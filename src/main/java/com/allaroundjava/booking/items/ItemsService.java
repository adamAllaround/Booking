package com.allaroundjava.booking.items;

import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.UUID;

@AllArgsConstructor
class ItemsService {
    private final ItemsRepository itemsRepository;
    private final OwnersRepository ownersRepository;
    Item save(Item item) {
        if (ownerNotExists(item.getOwnerId())) {
            throw new IllegalArgumentException(String.format("Owner with ID %s does not exist", item.getOwnerId()));
        }
        return itemsRepository.save(item);
    }

    private boolean ownerNotExists(UUID ownerId) {
        return ownersRepository.getSingle(ownerId).isEmpty();
    }

    Collection<Item> getAllByOwnerId(UUID ownerId) {
        return itemsRepository.getAllByOwnerId(ownerId);
    }
}
