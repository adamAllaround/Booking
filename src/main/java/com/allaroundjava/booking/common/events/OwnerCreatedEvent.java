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
    String ownerContactEmail;

    public static OwnerCreatedEvent now(UUID subjectId, String ownerContactEmail) {
        return new OwnerCreatedEvent(UUID.randomUUID(), Instant.now(), subjectId, ownerContactEmail);
    }
}
