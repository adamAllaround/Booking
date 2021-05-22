package com.allaroundjava.booking.common.events;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OwnerCreatedEvent implements DomainEvent {
    UUID eventId;
    Instant created;
    UUID subjectId;

    public static OwnerCreatedEvent now(UUID subjectId) {
        return new OwnerCreatedEvent(UUID.randomUUID(), Instant.now(), subjectId);
    }
}
