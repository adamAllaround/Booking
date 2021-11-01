package com.allaroundjava.booking.bookings.domain.model;

import lombok.AllArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class Availabilities {
    private final UUID itemId;
    private final TreeSet<Availability> availabilities;
    private final OffsetTime hotelHourStart;
    private final OffsetTime hotelHourEnd;

    public static Availabilities from(UUID id, OffsetTime hotelHourStart, OffsetTime hotelHourEnd, List<Availability> availabilities) {
        return new Availabilities(id, new TreeSet<>(availabilities), hotelHourStart, hotelHourEnd);
    }

    Optional<List<Availability>> tryAdd(Interval interval) {
        Instant intervalAtHotelStart = atHotelStart(interval.getStart());
        Instant intervalAtNextDayHotelEnd = atHotelEnd(nextDay(intervalAtHotelStart));
        Interval firstDay = new Interval(intervalAtHotelStart, intervalAtNextDayHotelEnd);

        Instant lastDayEndAtHotelHour = atHotelEnd(interval.getEnd());

        List<Availability> newAvailabilities = getBetween(firstDay, lastDayEndAtHotelHour);

        availabilities.addAll(newAvailabilities);

        return newAvailabilities.isEmpty() ? Optional.empty() : Optional.of(newAvailabilities);
    }

    private List<Availability> getBetween(Interval firstAvailability, Instant lastDayEndAtHotelHour) {
        return firstAvailability.multiplyTill(lastDayEndAtHotelHour)
                .stream()
                .filter(interval -> !overlapsExistingAvailabilities(interval))
                .map(interval -> Availability.from(itemId, interval))
                .collect(Collectors.toList());
    }

    private Instant atHotelStart(Instant instant) {
        return instant.atZone(ZoneOffset.UTC)
                .withHour(hotelHourStart.getHour())
                .toInstant();
    }

    private Instant atHotelEnd(Instant instant) {
        return instant.atZone(ZoneOffset.UTC)
                .withHour(hotelHourEnd.getHour())
                .toInstant();
    }

    private Instant nextDay(Instant intervalAtHotelStart) {
        return intervalAtHotelStart.atZone(ZoneOffset.UTC)
                .plus(1, ChronoUnit.DAYS).toInstant();
    }

    private boolean overlapsExistingAvailabilities(Interval seek) {
        return availabilities.stream().anyMatch(availability -> availability.overlaps(seek));
    }

    Availabilities matchingIds(Set<UUID> availabilityIds) {
        TreeSet<Availability> matchingIds = availabilitiesMatchingIds(availabilityIds);
        return new Availabilities(itemId, matchingIds, hotelHourStart, hotelHourEnd);
    }

    private TreeSet<Availability> availabilitiesMatchingIds(Set<UUID> availabilityIds) {
        return availabilities
                .stream()
                .filter(availability -> availabilityIds.contains(availability.getId())).collect(Collectors.toCollection(TreeSet::new));
    }

    boolean isContinuous() {
        if(availabilities.isEmpty()) return false;

        Availability seek = availabilities.first();

        for (Availability avail : availabilities) {
            if(dayDifference(seek, avail) > 1) {
                return false;
            }
            seek = avail;
        }
        return true;
    }

    private long dayDifference(Availability seek, Availability avail) {
        return Duration.between(avail.getStart(), seek.getStart()).abs().toDays();
    }

    Availabilities bookAll(UUID bookingId) {
        TreeSet<Availability> booked = availabilities.stream()
                .map(availability -> availability.book(bookingId))
                .collect(Collectors.toCollection(TreeSet::new));

        return new Availabilities(itemId, booked, hotelHourStart, hotelHourEnd);
    }

    boolean remove(Availability availability) {
        return availabilities.remove(availability);
    }
//TODO can put this value object in invalid state
    void removeAll(Availabilities candidates) {
        this.availabilities.removeAll(candidates.availabilities);
    }

    void addAll(Availabilities candidates) {
        this.availabilities.addAll(candidates.availabilities);
    }

    boolean isEmpty() {
        return availabilities.isEmpty();
    }

    boolean isAnyBooked() {
        return availabilities.stream().anyMatch(Availability::isBooked);
    }

    boolean anyEndsBefore(Instant now) {
        return availabilities.stream().anyMatch(availability -> availability.getEnd().isBefore(now));
    }
}
