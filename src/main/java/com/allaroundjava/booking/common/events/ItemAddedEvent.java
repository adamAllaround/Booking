package com.allaroundjava.booking.common.events;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemAddedEvent implements DomainEvent{
    UUID eventId;
    Instant created;
    UUID subjectId;

    public static ItemAddedEvent now(UUID subjectId) {
        return new ItemAddedEvent(UUID.randomUUID() , Instant.now(), subjectId);
    }
}
