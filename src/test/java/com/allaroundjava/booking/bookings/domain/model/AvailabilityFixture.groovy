package com.allaroundjava.booking.bookings.domain.model

import static com.allaroundjava.booking.bookings.domain.model.Dates2020.*

class AvailabilityFixture {
    public static UUID ITEM_ID = UUID.randomUUID()
    static Availability MAY10 = new Availability(UUID.randomUUID(), ITEM_ID, new Interval(may(10).hour(11), may(11).hour(10)))
    static Availability MAY11 = new Availability(UUID.randomUUID(), ITEM_ID, new Interval(may(11).hour(11), may(12).hour(10)))
    static Availability MAY12 = new Availability(UUID.randomUUID(), ITEM_ID, new Interval(may(12).hour(11), may(13).hour(10)))
}
