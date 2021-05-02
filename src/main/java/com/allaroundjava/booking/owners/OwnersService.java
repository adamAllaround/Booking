package com.allaroundjava.booking.owners;

import com.allaroundjava.booking.common.events.EventPublisher;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
class OwnersService {
    private final OwnersRepository repository;
    private final EventPublisher eventPublisher;

    Collection<Owner> getAll() {
        return repository.getAll();
    }

    Optional<Owner> getSingle(Long id) {
        return repository.getSingle(id);
    }

    Owner save(Owner owner) {
        Owner newOwner = repository.save(owner);
        eventPublisher.publish(new OwnerCreatedEvent(newOwner));

        return newOwner;
    }
}
