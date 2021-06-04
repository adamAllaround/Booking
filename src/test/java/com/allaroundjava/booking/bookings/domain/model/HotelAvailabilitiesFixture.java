package com.allaroundjava.booking.bookings.domain.model;

import java.time.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

class HotelAvailabilitiesFixture {
    static final UUID ITEM_ID = UUID.randomUUID();
    static final OffsetTime STANDARD_HOTEL_START = OffsetTime.of(15, 0, 0, 0, ZoneOffset.UTC);
    static final OffsetTime STANDARD_HOTEL_END = OffsetTime.of(10, 0, 0, 0, ZoneOffset.UTC);

    public static Availabilities standardEmpty() {
        return new HotelAvailabilities(ITEM_ID, new ArrayList<>(), STANDARD_HOTEL_START, STANDARD_HOTEL_END);
    }

    public static Availabilities withExistingIntervals(Collection<Interval> intervals) {
        List<Availability> availabilities = intervals.stream()
                .map(HotelAvailabilitiesFixture::availabilityFromInterval)
                .collect(Collectors.toList());
        return new HotelAvailabilities(ITEM_ID, availabilities, STANDARD_HOTEL_START, STANDARD_HOTEL_END);
    }

    public static Availabilities withExistingInterval(Interval interval) {
        ArrayList<Interval> intervals = new ArrayList<>();
        intervals.add(interval);
        return withExistingIntervals(intervals);
    }

    private static Availability availabilityFromInterval(Interval interval) {
        LocalDate start = LocalDate.ofInstant(interval.getStart(), ZoneOffset.UTC);
        LocalDate end = LocalDate.ofInstant(interval.getEnd(), ZoneOffset.UTC);
        OffsetDateTime startDateTime = start.atTime(STANDARD_HOTEL_START);
        OffsetDateTime endDateTime = end.atTime(STANDARD_HOTEL_END);

        return new Availability(UUID.randomUUID(), UUID.randomUUID(), new Interval(startDateTime.toInstant(), endDateTime.toInstant()));
    }
}
