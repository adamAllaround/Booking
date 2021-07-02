package com.allaroundjava.booking.bookings.adapters.db;

import com.allaroundjava.booking.bookings.domain.model.Booking;
import com.allaroundjava.booking.bookings.domain.model.Interval;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.UUID;

@Data
public class BookingDatabaseEntity {
    UUID id;
    UUID itemId;
    OffsetDateTime startTime;
    OffsetDateTime endTime;

    Booking toModel() {
        return new Booking(id, itemId, new Interval(startTime.toInstant(), endTime.toInstant()), new HashSet<>());
    }
}
