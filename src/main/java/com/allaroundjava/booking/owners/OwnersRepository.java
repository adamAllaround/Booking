package com.allaroundjava.booking.owners;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

class OwnersRepository {
    Collection<Owner> getAll() {
        return Collections.emptyList();
    }

    Optional<Owner> getSingle(Long id) {
        return Optional.empty();
    }

    Owner save(Owner owner) {
        return owner;
    }
}
