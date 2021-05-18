package com.allaroundjava.booking.bookings.domain.model;

import lombok.Value;

import java.util.UUID;

@Value
public class Booking {
    UUID id;
    UUID itemId;
    Interval interval;

    static Booking from(Availability availability) {
        return new Booking(availability.getId(), availability.getItemId(), availability.getInterval());
    }
}
