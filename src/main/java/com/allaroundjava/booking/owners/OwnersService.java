package com.allaroundjava.booking.owners;

import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
class OwnersService {
    private final OwnersRepository repository;
    private final MessageSender messageSender;

    Collection<Owner> getAll() {
        return repository.getAll();
    }

    Optional<Owner> getSingle(Long id) {
        return repository.getSingle(id);
    }

    Owner save(Owner owner) {
        Owner newOwner = repository.save(owner);
        messageSender.send(new OwnerCreatedMessage(newOwner.getId()));

        return newOwner;
    }
}
