package com.allaroundjava.booking.bookings.domain.model;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

class HotelAvailabilitiesFixture {

    public static Availabilities standardEmpty() {
        return Availabilities.from(RoomFixture.getROOM_DETAILS(), new ArrayList<>());
    }

    public static Availabilities withExistingIntervals(Collection<Interval> intervals) {
        List<Availability> availabilities = intervals.stream()
                .map(HotelAvailabilitiesFixture::availabilityFromInterval)
                .collect(Collectors.toList());
        return Availabilities.from(RoomFixture.getROOM_DETAILS(), availabilities);
    }

    public static Availabilities withExistingInterval(Interval interval) {
        ArrayList<Interval> intervals = new ArrayList<>();
        intervals.add(interval);
        return withExistingIntervals(intervals);
    }

    private static Availability availabilityFromInterval(Interval interval) {
        LocalDate start = LocalDate.ofInstant(interval.getStart(), ZoneOffset.UTC);
        LocalDate end = LocalDate.ofInstant(interval.getEnd(), ZoneOffset.UTC);
        OffsetDateTime startDateTime = start.atTime(RoomFixture.getROOM_DETAILS().getHotelHourStart());
        OffsetDateTime endDateTime = end.atTime(RoomFixture.getROOM_DETAILS().getHotelHourEnd());

        return new Availability(UUID.randomUUID(), UUID.randomUUID(), new Interval(startDateTime.toInstant(), endDateTime.toInstant()));
    }

    public static Availabilities withConcreteAvailabilityList(List<Availability> availabilities) {
        return Availabilities.from(RoomFixture.getROOM_DETAILS(), availabilities);
    }
}
