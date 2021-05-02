package com.allaroundjava.booking.common.events;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@AllArgsConstructor
public class OwnerCreatedEvent implements DomainEvent {
    UUID eventId;
    Instant created;
    Long subjectId;

    public static OwnerCreatedEvent createNew(Long subjectId) {
        return new OwnerCreatedEvent(UUID.randomUUID(), Instant.now(), subjectId);
    }
}
