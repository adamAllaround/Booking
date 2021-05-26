package com.allaroundjava.booking.common.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public abstract class ItemCreatedEvent implements DomainEvent{
    protected UUID eventId;
    protected Instant created;
    protected UUID subjectId;
}

