package com.allaroundjava.booking.items;

import com.allaroundjava.booking.common.events.OwnerCreatedEvent;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;

@Log4j2
@AllArgsConstructor
class OwnerCreatedEventHandler {
    private final OwnersRepository ownersRepository;

    @EventListener
    public void handle(OwnerCreatedEvent event) {
        log.info("Received new OwnerCreatedEvent {}", event);
        ownersRepository.save(new Owner(event.getSubjectId(), event.getCreated()));
        log.info("Persisted OwnerCreatedEvent {}", event.getEventId());
    }
}
