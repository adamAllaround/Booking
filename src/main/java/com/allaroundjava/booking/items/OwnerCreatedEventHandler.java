package com.allaroundjava.booking.items;

import com.allaroundjava.booking.common.events.OwnerCreatedEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;

@AllArgsConstructor
class OwnerCreatedEventHandler {
    private final OwnersRepository ownersRepository;

    @EventListener
    public void handle(OwnerCreatedEvent event) {
        ownersRepository.save(new Owner(event.getSubjectId(), event.getCreated()));
        System.out.println("Received event with new owher ID=" + event.getSubjectId());
    }
}
