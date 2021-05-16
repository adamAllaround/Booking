package com.allaroundjava.booking.bookings.adapters.db;

import com.allaroundjava.booking.bookings.domain.model.Booking;
import org.springframework.data.annotation.Id;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

public class BookingDatabaseEntity {
    @Id
    Long id;
    UUID itemId;
    UUID bookingId;
    OffsetDateTime start;
    OffsetDateTime end;

    Booking toModel() {
        return new Booking(bookingId, start.toInstant(), end.toInstant());
    }
}
