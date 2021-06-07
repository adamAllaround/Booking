package com.allaroundjava.booking.bookings.adapters.db;

import com.allaroundjava.booking.bookings.domain.model.Availability;
import com.allaroundjava.booking.bookings.domain.model.Interval;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
class AvailabilityDatabaseEntity {
    UUID id;
    UUID itemId;
    OffsetDateTime start;
    OffsetDateTime end;
    UUID bookingId;

    Availability toModel() {
        Availability availability = new Availability(id, itemId, new Interval(start.toInstant(), end.toInstant()));
        if (bookingId == null) {
            return availability;
        }
        return availability.book(bookingId);
    }
}
