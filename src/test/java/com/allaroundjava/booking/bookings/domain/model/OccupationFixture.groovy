package com.allaroundjava.booking.bookings.domain.model

import java.time.LocalDateTime

class OccupationFixture {
    static Occupation emptyOccupation() {
        new Occupation(new Item(UUID.randomUUID()))
    }

    static Occupation withAvailabilityBetween(LocalDateTime start, LocalDateTime end) {
        def occupation = new Occupation(new Item(UUID.randomUUID()))
        occupation.addAvailability(new Interval(start, end))

        return occupation
    }

    static Occupation withBookingBetween(LocalDateTime start, LocalDateTime end) {
        def occupation = withAvailabilityBetween(start, end)
        occupation.addBooking(new Interval(start, end))

        return occupation
    }
}
