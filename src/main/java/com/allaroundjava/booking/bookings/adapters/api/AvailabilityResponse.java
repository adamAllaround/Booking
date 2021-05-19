package com.allaroundjava.booking.bookings.adapters.api;

import com.allaroundjava.booking.bookings.domain.model.Availability;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Data
@AllArgsConstructor
class AvailabilityResponse {
    UUID id;
    UUID ownerId;
    OffsetDateTime start;
    OffsetDateTime end;

    static AvailabilityResponse from(Availability availability) {
        return new AvailabilityResponse(availability.getId(),
                availability.getItemId(),
                OffsetDateTime.ofInstant(availability.getStart(), ZoneOffset.UTC),
                OffsetDateTime.ofInstant(availability.getEnd(), ZoneOffset.UTC));
    }
}
