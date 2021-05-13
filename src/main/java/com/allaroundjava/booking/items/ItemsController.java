package com.allaroundjava.booking.items;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/owners")
@AllArgsConstructor
class ItemsController {
    private final ItemsService itemsService;

    @GetMapping("/{ownerId}/items")
    ResponseEntity<ItemsResponse> ownersItems(@PathVariable UUID ownerId) {
        return ResponseEntity.ok(ItemsResponse.from(itemsService.getAllByOwnerId(ownerId)));
    }

    @PostMapping("/{ownerId}/items")
    ResponseEntity<ItemResponse> addItem(@PathVariable UUID ownerId, @RequestBody ItemRequest itemRequest) {
        Item item = itemsService.save(itemRequest.toModelWithOwner(ownerId));
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(item.getId()).toUri();
        return ResponseEntity.created(uri)
                .body(ItemResponse.from(item));
    }

    @Data
    @AllArgsConstructor
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
            return new ItemResponse(item.getId(), item.getName(), item.getCapacity(), item.getLocation());
        }
    }

    @Data
    static class ItemRequest {
        UUID ownerId;
        String name;
        int capacity;
        String location;

        Item toModelWithOwner(UUID ownerId) {
            Item item = new Item();
            item.setOwnerId(ownerId);
            item.setName(name);
            item.setCapacity(capacity);
            item.setLocation(location);
            return item;
        }
    }
}

