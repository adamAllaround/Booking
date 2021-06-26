package com.allaroundjava.booking.common.events;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@AllArgsConstructor
public class OwnerCreatedEvent implements DomainEvent {
    UUID eventId;
    Instant created;
    UUID subjectId;

    public static OwnerCreatedEvent now(UUID subjectId) {
        return new OwnerCreatedEvent(UUID.randomUUID(), Instant.now(), subjectId);
    }
}
