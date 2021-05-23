package com.allaroundjava.booking.common.events;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@AllArgsConstructor
public class ItemCreatedEvent implements DomainEvent{
    UUID eventId;
    Instant created;
    UUID subjectId;

    public static ItemCreatedEvent now(UUID subjectId) {
        return new ItemCreatedEvent(UUID.randomUUID() , Instant.now(), subjectId);
    }
}
