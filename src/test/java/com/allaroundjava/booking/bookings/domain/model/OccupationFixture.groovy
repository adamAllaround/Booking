package com.allaroundjava.booking.bookings.domain.model

import java.time.Instant

import static com.allaroundjava.booking.bookings.domain.model.HotelAvailabilitiesFixture.standardEmpty
import static com.allaroundjava.booking.bookings.domain.model.HotelAvailabilitiesFixture.withExistingInterval

class OccupationFixture {
    static Occupation emptyOccupation() {
        new Occupation(UUID.randomUUID(), [], standardEmpty())
    }

    static Occupation withAvailabilityBetween(Instant start, Instant end) {
        def itemId = UUID.randomUUID()
        def availability = withExistingInterval(new Interval(start, end))

        return create(itemId, availability)
    }

    static Occupation withBookingBetween(Instant start, Instant end) {
        def itemId = UUID.randomUUID()
        Availabilities availabilities = standardEmpty()
        List<Booking> bookings = [new Booking(UUID.randomUUID(), itemId, new Interval(start, end))]
        return create(itemId, availabilities, bookings)
    }

    private static Occupation create(UUID itemId, Availabilities availabilities, ArrayList<Booking> bookings) {
        return new Occupation(itemId, bookings, availabilities)
    }

    private static Occupation create(UUID itemId, Availabilities availabilities) {
        return new Occupation(itemId, new ArrayList<Booking>(), availabilities)
    }

}
