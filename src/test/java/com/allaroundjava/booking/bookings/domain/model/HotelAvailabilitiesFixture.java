package com.allaroundjava.booking.bookings.domain.model;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

class HotelAvailabilitiesFixture {
    static final UUID ITEM_ID = UUID.randomUUID();
    static final OffsetTime STANDARD_HOTEL_START = OffsetTime.of(15, 0, 0, 0, ZoneOffset.UTC);
    static final OffsetTime STANDARD_HOTEL_END = OffsetTime.of(10, 0, 0, 0, ZoneOffset.UTC);
    static final Item item = new Item(ITEM_ID, ItemType.HotelRoom, STANDARD_HOTEL_START, STANDARD_HOTEL_END);

    public static Availabilities standardEmpty() {
        return Availabilities.from(item, new ArrayList<>());
    }

    public static Availabilities withExistingIntervals(Collection<Interval> intervals) {
        List<Availability> availabilities = intervals.stream()
                .map(HotelAvailabilitiesFixture::availabilityFromInterval)
                .collect(Collectors.toList());
        return Availabilities.from(item, availabilities);
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

    public static Availabilities withConcreteAvailabilityList(List<Availability> availabilities) {
        return Availabilities.from(item, availabilities);
    }
}
