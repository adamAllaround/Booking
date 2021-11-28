package com.allaroundjava.booking.bookings.adapters.api;

import com.allaroundjava.booking.bookings.domain.command.AddAvailabilityCommand;
import com.allaroundjava.booking.bookings.domain.model.Interval;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
class AvailabilityRequest {
    OffsetDateTime start;
    OffsetDateTime end;

    AddAvailabilityCommand toCommand(UUID itemId) {
        return new AddAvailabilityCommand(itemId, new Interval(start.toInstant(), end.toInstant()));
    }
}
