package com.allaroundjava.booking.bookings.domain.model


import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.stream.Collectors

class AvailabilitiesFixture {

    static Availabilities standardEmpty() {
        return Availabilities.from(RoomFixture.getROOM_ID(), RoomFixture.getSTANDARD_HOTEL_START(), RoomFixture.getSTANDARD_HOTEL_END(), new ArrayList<>());
    }

    static Availabilities withExistingIntervals(Collection<Interval> intervals) {
        List<Availability> availabilities = intervals.stream()
                .map {availabilityFromInterval(it) }
                .collect(Collectors.toList());
        return Availabilities.from(RoomFixture.getROOM_ID(), RoomFixture.getSTANDARD_HOTEL_START(), RoomFixture.getSTANDARD_HOTEL_END(), availabilities);
    }

    static Availabilities withExistingInterval(Interval interval) {
        ArrayList<Interval> intervals = new ArrayList<>();
        intervals.add(interval);
        return withExistingIntervals(intervals);
    }

    private static Availability availabilityFromInterval(Interval interval) {
        LocalDate start = LocalDate.ofInstant(interval.getStart(), ZoneOffset.UTC);
        LocalDate end = LocalDate.ofInstant(interval.getEnd(), ZoneOffset.UTC);
        OffsetDateTime startDateTime = start.atTime(RoomFixture.getSTANDARD_HOTEL_START());
        OffsetDateTime endDateTime = end.atTime(RoomFixture.getSTANDARD_HOTEL_END());

        return new Availability(UUID.randomUUID(), UUID.randomUUID(), new Interval(startDateTime.toInstant(), endDateTime.toInstant()));
    }

    static Availabilities withConcreteAvailabilityList(List<Availability> availabilities) {
        return Availabilities.from(RoomFixture.getROOM_ID(), RoomFixture.getSTANDARD_HOTEL_START(), RoomFixture.getSTANDARD_HOTEL_END(), availabilities);
    }
}
