package com.allaroundjava.booking.bookings.domain.model


import java.time.Clock
import java.time.Instant

import static com.allaroundjava.booking.bookings.domain.model.HotelAvailabilitiesFixture.standardEmpty
import static com.allaroundjava.booking.bookings.domain.model.HotelAvailabilitiesFixture.withExistingInterval

class OccupationFixture {
    private Clock clock

    Occupation empty() {
        new Occupation(UUID.randomUUID(), [], standardEmpty(), BookingPolicies.allHotelRoomPolicies(clock))
    }

    static OccupationFixture withClock(Clock aClock) {
        return new OccupationFixture(clock: aClock)
    }

    Occupation andAvailabilityBetween(Instant start, Instant end) {
        def itemId = UUID.randomUUID()
        def availability = withExistingInterval(new Interval(start, end))

        return create(itemId, availability)
    }

    Occupation andConcreteAvailabilities(Availabilities availabilities) {
        def itemId = UUID.randomUUID()
        return create(itemId, availabilities)
    }

    private Occupation create(UUID itemId, Availabilities availabilities) {
        return new Occupation(itemId, new ArrayList<Booking>(), availabilities, BookingPolicies.allHotelRoomPolicies(clock))
    }
}
