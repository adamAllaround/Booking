package com.allaroundjava.booking.bookings.adapters.api;

import com.allaroundjava.booking.bookings.domain.model.Booking;
import com.allaroundjava.booking.bookings.domain.model.Interval;
import lombok.Data;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

@Data
class BookingRequest {
    UUID itemId;
    String firstName;
    String lastName;
    Interval interval;
    Collection<UUID> availabilities;

    Booking toDomain() {
        return new Booking(UUID.randomUUID(),
                itemId,
                interval,
                new HashSet<>(availabilities));
    }
}