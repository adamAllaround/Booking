package com.allaroundjava.booking.bookings.domain.model


import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.ZoneOffset
import java.util.stream.Collectors

import static com.allaroundjava.booking.bookings.domain.model.HotelAvailabilitiesFixture.standardEmpty
import static com.allaroundjava.booking.bookings.domain.model.HotelAvailabilitiesFixture.withExistingInterval

class RoomFixture {
    private Clock clock
    static final OffsetTime STANDARD_HOTEL_START = OffsetTime.of(15, 0, 0, 0, ZoneOffset.UTC)
    static final OffsetTime STANDARD_HOTEL_END = OffsetTime.of(10, 0, 0, 0, ZoneOffset.UTC)
    static final RoomDetails ROOM_DETAILS = new RoomDetails(UUID.randomUUID(), STANDARD_HOTEL_START, STANDARD_HOTEL_END)

    static RoomOccupation empty() {
        new RoomOccupation(ROOM_DETAILS.id,
                standardEmpty(),
                BookingsFixture.empty(),
                BookingPolicies.allHotelRoomPolicies(Dates2020.JAN_CLOCK))
    }

    static RoomFixture withClock(Clock aClock) {
        return new RoomFixture(clock: aClock)
    }

    RoomOccupation andAvailabilityBetween(Instant start, Instant end) {
        def availability = withExistingInterval(new Interval(start, end))

        return create(availability)
    }

    RoomOccupation andConcreteAvailabilities(Availabilities availabilities) {
        return create(availabilities)
    }

    private RoomOccupation create(Availabilities availabilities) {
        return new RoomOccupation(ROOM_DETAILS.id,
                availabilities,
                BookingsFixture.empty(), BookingPolicies.allHotelRoomPolicies(clock))
    }

    RoomOccupation andAvailabilitiesInIntervals(Collection<Interval> intervals) {
        List<Availability> availabilities = intervals.stream()
                .map { it -> availabilityFromInterval(it) }
                .collect(Collectors.toList())
        return this
                .andConcreteAvailabilities(Availabilities.from(ROOM_DETAILS, availabilities))
    }

    private static Availability availabilityFromInterval(Interval interval) {
        LocalDate start = LocalDate.ofInstant(interval.getStart(), ZoneOffset.UTC)
        LocalDate end = LocalDate.ofInstant(interval.getEnd(), ZoneOffset.UTC)
        OffsetDateTime startDateTime = start.atTime(STANDARD_HOTEL_START)
        OffsetDateTime endDateTime = end.atTime(STANDARD_HOTEL_END)

        return new Availability(UUID.randomUUID(), UUID.randomUUID(), new Interval(startDateTime.toInstant(), endDateTime.toInstant()))
    }
}
