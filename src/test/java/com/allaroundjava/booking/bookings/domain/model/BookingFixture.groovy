package com.allaroundjava.booking.bookings.domain.model

class BookingFixture {
    static Booking fromSingleAvailability(Availability availability) {
        return new Booking(UUID.randomUUID(), availability.getItemId(), "test@booker.email", availability.getInterval(), [availability.getId()].toSet())
    }
}
