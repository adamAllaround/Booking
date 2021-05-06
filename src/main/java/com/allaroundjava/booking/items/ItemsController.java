package com.allaroundjava.booking.items;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/owners")
@AllArgsConstructor
class ItemsController {
    private final ItemsRepository itemsRepository;

    @GetMapping("/{ownerId}/items")
    ResponseEntity<ItemsResponse> ownersItems(@PathVariable UUID ownerId) {
        return ResponseEntity.ok(ItemsResponse.from(itemsRepository.getAllByOwnerId(ownerId)));
    }

    @Value
    static class ItemsResponse {
        Collection<ItemResponse> items;

        static ItemsResponse from(Collection<Item> items) {
            List<ItemResponse> collection = items.stream()
                    .map(ItemResponse::from)
                    .collect(Collectors.toUnmodifiableList());
            return new ItemsResponse(collection);
        }

    }

    @Value
    static class ItemResponse {
        UUID id;
        String name;
        int capacity;
        String location;

        static ItemResponse from(Item item) {
            return new ItemResponse(item.getUuid(), item.getName(), item.getCapacity(), item.getLocation());
        }
    }
}

