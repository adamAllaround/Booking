package com.allaroundjava.booking.bookings.domain.model;

import com.allaroundjava.booking.common.events.DomainEvent;
import lombok.NonNull;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

public interface OccupationEvent extends DomainEvent {

    UUID getItemId();

    default UUID getSubjectId() {
        return getItemId();
    }

    @Value
    class AddAvailabilityFailure implements OccupationEvent {
        UUID eventId = UUID.randomUUID();
        Instant created = Instant.now();
        @NonNull UUID itemId;
        @NonNull String reason;
    }

    @Value
    class AddAvailabilitySuccess implements OccupationEvent {
        UUID eventId = UUID.randomUUID();
        Instant created = Instant.now();
        @NonNull UUID itemId;
        @NonNull Availability availability;
    }

    @Value
    class RemoveAvailabilitySuccess implements OccupationEvent {
        UUID eventId = UUID.randomUUID();
        Instant created = Instant.now();
        @NonNull UUID itemId;
        @NonNull UUID availabilityId;
    }

    @Value
    class RemoveAvailabilityFailure implements OccupationEvent {
        UUID eventId = UUID.randomUUID();
        Instant created = Instant.now();
        @NonNull UUID itemId;
        @NonNull UUID availabilityId;
        @NonNull String reason;
    }



}