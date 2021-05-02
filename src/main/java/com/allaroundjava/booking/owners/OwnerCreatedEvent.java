package com.allaroundjava.booking.owners;

import com.allaroundjava.booking.common.events.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@AllArgsConstructor
class OwnerCreatedEvent implements DomainEvent {
    UUID eventId = UUID.randomUUID();
    Instant when = Instant.now();
    Long subjectId;

    OwnerCreatedEvent(Owner owner) {
        this(owner.getId());
    }
}
