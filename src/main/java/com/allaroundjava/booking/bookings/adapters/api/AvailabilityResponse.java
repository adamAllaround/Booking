package com.allaroundjava.booking.bookings.adapters.api;

import com.allaroundjava.booking.bookings.domain.model.Availability;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
class AvailabilityResponse {
    UUID id;
    UUID itemId;
    OffsetDateTime start;
    OffsetDateTime end;

    static AvailabilityResponse from(Availability availability) {
        return new AvailabilityResponse(availability.getId(),
                availability.getItemId(),
                OffsetDateTime.ofInstant(availability.getStart(), ZoneOffset.UTC),
                OffsetDateTime.ofInstant(availability.getEnd(), ZoneOffset.UTC));
    }
}
