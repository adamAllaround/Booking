package com.allaroundjava.booking.bookings.domain.model

import java.time.Instant

class OccupationFixture {
    static Occupation emptyOccupation() {
        new Occupation(new Item(UUID.randomUUID()))
    }

    static Occupation withAvailabilityBetween(Instant start, Instant end) {
        def itemId = UUID.randomUUID()
        Availabilities availabilities = Availabilities.empty()
        availabilities.add(new Availability(UUID.randomUUID(), itemId, new Interval(start, end)))
        return create(itemId, availabilities)
    }

    static Occupation withBookingBetween(Instant start, Instant end) {
        def itemId = UUID.randomUUID()
        Availabilities availabilities = Availabilities.empty()
        List<Booking> bookings = [new Booking(UUID.randomUUID(), itemId, new Interval(start, end))]
        return create(itemId, availabilities, bookings)
    }

    private static Occupation create(UUID itemId, Availabilities availabilities, ArrayList<Booking> bookings) {
        return new Occupation(new Item(itemId), bookings, availabilities)
    }

    private static Occupation create(UUID itemId, Availabilities availabilities) {
        return new Occupation(new Item(itemId), new ArrayList<Booking>(), availabilities)
    }

}
