package com.allaroundjava.booking.common.events;

import java.time.Instant;
import java.util.UUID;

public interface DomainEvent {
    UUID getEventId();

    UUID getSubjectId();

    Instant getCreated();
}
