package com.allaroundjava.booking.owners;

import com.allaroundjava.booking.common.events.EventPublisher;
import com.allaroundjava.booking.common.events.OwnerCreatedEvent;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
class OwnersService {
    private final OwnersRepository repository;
    private final EventPublisher eventPublisher;

    Collection<Owner> getAll() {
        return repository.getAll();
    }

    Optional<Owner> getSingle(UUID id) {
        return repository.getSingle(id);
    }

    Owner save(Owner owner) {
        Owner newOwner = repository.save(owner);
        eventPublisher.publish(OwnerCreatedEvent.now(newOwner.getId(), owner.getEmail()));
        return newOwner;
    }
}
