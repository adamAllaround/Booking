package com.allaroundjava.booking.bookings.domain.model;

import lombok.AllArgsConstructor;

import java.time.Instant;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public abstract class Availabilities {
    protected final List<Availability> availabilities;

    public static Availabilities from(ItemType itemType, OffsetTime hotelHourStart, OffsetTime hotelHourEnd, List<Availability> availabilities) {
        return new HotelAvailabilities(availabilities, hotelHourStart, hotelHourEnd);
    }

    abstract Optional<List<Availability>> tryAdd(Interval interval);

    boolean remove(Availability availability) {
        return availabilities.remove(availability);
    }

    Optional<Availability> findCovering(Interval interval) {
        return availabilities.stream()
                .filter(avail -> avail.covers(interval))
                .findFirst();
    }

}

class HotelAvailabilities extends Availabilities {

    private final OffsetTime hotelHourStart;
    private final OffsetTime hotelHourEnd;

    public HotelAvailabilities(List<Availability> availabilities, OffsetTime hotelHourStart, OffsetTime hotelHourEnd) {
        super(availabilities);
        this.hotelHourStart = hotelHourStart;
        this.hotelHourEnd = hotelHourEnd;
    }

    @Override
    Optional<List<Availability>> tryAdd(Interval interval) {
        Instant intervalAtHotelStart = interval.getStart().atZone(ZoneOffset.UTC)
                .withHour(hotelHourStart.getHour())
                .toInstant();
        Instant intervalAtNextDayHotelEnd = intervalAtHotelStart.atZone(ZoneOffset.UTC)
                .plus(1, ChronoUnit.DAYS)
                .withHour(hotelHourEnd.getHour())
                .toInstant();

        Instant lastDayEndAtHotelHour = interval.getEnd().atZone(ZoneOffset.UTC).withHour(hotelHourEnd.getHour()).toInstant();

        Interval seek = new Interval(intervalAtHotelStart, intervalAtNextDayHotelEnd);

        List<Availability> newAvailabilities = new LinkedList<>();

        while (!seek.getEnd().isAfter(lastDayEndAtHotelHour)) {
            if(!overlaps(seek)) {
                newAvailabilities.add(Availability.from(UUID.randomUUID(), seek));
            }
            seek = seek.plusDays(1);
        }

        availabilities.addAll(newAvailabilities);

        return newAvailabilities.isEmpty() ? Optional.empty() : Optional.of(newAvailabilities);
    }

    private boolean overlaps(Interval seek) {
        return availabilities.stream().anyMatch(availability -> availability.overlaps(seek));
    }
}
