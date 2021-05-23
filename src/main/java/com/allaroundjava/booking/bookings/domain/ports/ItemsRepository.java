package com.allaroundjava.booking.bookings.domain.ports;

import com.allaroundjava.booking.bookings.domain.model.Item;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface ItemsRepository {
    void saveNew(UUID itemId, Instant created);

    Optional<Item> findById(UUID itemId);
}
