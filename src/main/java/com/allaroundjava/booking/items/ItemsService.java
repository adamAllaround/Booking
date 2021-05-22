package com.allaroundjava.booking.items;

import com.allaroundjava.booking.common.events.EventPublisher;
import com.allaroundjava.booking.common.events.ItemAddedEvent;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.UUID;

@AllArgsConstructor
class ItemsService {
    private final ItemsRepository itemsRepository;
    private final OwnersRepository ownersRepository;
    private final EventPublisher eventPublisher;

    Item save(Item item) {
        if (ownerNotExists(item.getOwnerId())) {
            throw new IllegalArgumentException(String.format("Owner with ID %s does not exist", item.getOwnerId()));
        }
        Item newItem = itemsRepository.save(item);
        eventPublisher.publish(ItemAddedEvent.now(newItem.id));
        return newItem;
    }

    private boolean ownerNotExists(UUID ownerId) {
        return ownersRepository.getSingle(ownerId).isEmpty();
    }

    Collection<Item> getAllByOwnerId(UUID ownerId) {
        return itemsRepository.getAllByOwnerId(ownerId);
    }
}
