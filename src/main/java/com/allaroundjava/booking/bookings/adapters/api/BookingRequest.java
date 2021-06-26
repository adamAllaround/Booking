package com.allaroundjava.booking.bookings.adapters.api;

import com.allaroundjava.booking.bookings.domain.model.Booking;
import com.allaroundjava.booking.bookings.domain.model.Interval;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

@Data
class BookingRequest {
    UUID itemId;
    String firstName;
    String lastName;
    OffsetDateTime start;
    OffsetDateTime end;
    Collection<UUID> availabilities;

    Booking toDomain() {
        return new Booking(UUID.randomUUID(),
                itemId,
                new Interval(start.toInstant(), end.toInstant()),
                new HashSet<>(availabilities));
    }
}