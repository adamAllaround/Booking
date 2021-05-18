package com.allaroundjava.booking.bookings.adapters.db;

import com.allaroundjava.booking.bookings.domain.model.Booking;
import com.allaroundjava.booking.bookings.domain.model.Interval;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class BookingDatabaseEntity {
    UUID id;
    UUID itemId;
    OffsetDateTime start;
    OffsetDateTime end;

    Booking toModel() {
        return new Booking(id, itemId, new Interval(start.toInstant(), end.toInstant()));
    }
}
