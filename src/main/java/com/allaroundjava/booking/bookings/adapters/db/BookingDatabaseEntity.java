package com.allaroundjava.booking.bookings.adapters.db;

import com.allaroundjava.booking.bookings.domain.model.Booking;
import com.allaroundjava.booking.bookings.domain.model.Interval;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class BookingDatabaseEntity {
    UUID id;
    UUID itemId;
    OffsetDateTime start;
    OffsetDateTime end;

    Booking toModel() {
        return new Booking(id, itemId, new Interval(start.toInstant(), end.toInstant()));
    }
}
