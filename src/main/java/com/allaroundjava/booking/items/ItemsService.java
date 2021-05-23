package com.allaroundjava.booking.items;

import com.allaroundjava.booking.common.events.EventPublisher;
import com.allaroundjava.booking.common.events.ItemCreatedEvent;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;

@AllArgsConstructor
class ItemsService {
    private final ItemsRepository itemsRepository;
    private final OwnersRepository ownersRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    Item save(Item item) {
        if (ownerNotExists(item.getOwnerId())) {
            throw new IllegalArgumentException(String.format("Owner with ID %s does not exist", item.getOwnerId()));
        }
        Item newItem = itemsRepository.save(item);
        eventPublisher.publish(ItemCreatedEvent.now(newItem.id));
        return newItem;
    }

    private boolean ownerNotExists(UUID ownerId) {
        return ownersRepository.getSingle(ownerId).isEmpty();
    }

    Collection<Item> getAllByOwnerId(UUID ownerId) {
        return itemsRepository.getAllByOwnerId(ownerId);
    }
}
