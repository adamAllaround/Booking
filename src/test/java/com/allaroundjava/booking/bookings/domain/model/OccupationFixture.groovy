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
}
