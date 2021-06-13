package com.allaroundjava.booking.bookings.domain.model;

import java.time.Instant;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

class HotelAvailabilities extends Availabilities {

    private final OffsetTime hotelHourStart;
    private final OffsetTime hotelHourEnd;

    public HotelAvailabilities(UUID itemId, List<Availability> availabilities, OffsetTime hotelHourStart, OffsetTime hotelHourEnd) {
        super(itemId, availabilities);
        this.hotelHourStart = hotelHourStart;
        this.hotelHourEnd = hotelHourEnd;
    }

    @Override
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
}
