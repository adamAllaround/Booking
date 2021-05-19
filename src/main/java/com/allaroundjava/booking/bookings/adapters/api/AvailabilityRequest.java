package com.allaroundjava.booking.bookings.adapters.api;

import com.allaroundjava.booking.bookings.domain.model.Availability;
import com.allaroundjava.booking.bookings.domain.model.Interval;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
class AvailabilityRequest {
    OffsetDateTime start;
    OffsetDateTime end;

    Availability toDomainWithItemId(UUID itemId) {
        return Availability.from(itemId, new Interval(start.toInstant(), end.toInstant()));
    }
}
