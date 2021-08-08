package com.allaroundjava.booking.notifications.owners;

import com.allaroundjava.booking.common.events.OwnerCreatedEvent;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.Instant;

@Log4j2
@AllArgsConstructor
class OwnerCreatedEventHandler {
    private final OwnersRepository ownersRepository;

    public void handle(OwnerCreatedEvent event) {
        log.info("Received new OwnerCreated event {}", event);
        ownersRepository.save(new Owner(event.getSubjectId(), event.getOwnerContactEmail()));
        log.info("Persisted new owner {} details in notification module. Event Id {}", event.getSubjectId(), event.getEventId());
    }
}
