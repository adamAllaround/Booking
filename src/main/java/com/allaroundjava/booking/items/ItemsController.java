package com.allaroundjava.booking.items;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.OffsetTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


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
        ItemDetails details;

        Item toModelWithOwner(UUID ownerId) {
            Item item = new Item();
            item.setOwnerId(ownerId);
            item.setName(name);
            details.fill(item);
            return item;
        }
    }

    @Data
    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = HotelRoomDetails.class, name = "hotelRoom"),
    })
    static abstract class ItemDetails {
        int capacity;
        String location;

        void fill(Item item) {
            item.setCapacity(capacity);
            item.setLocation(location);
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    static class HotelRoomDetails extends ItemDetails{
        OffsetTime hotelHourStart;
        OffsetTime hotelHourEnd;

        void fill(Item item) {
            super.fill(item);
            item.setHotelHourStart(hotelHourStart);
            item.setHotelHourEnd(hotelHourEnd);
            item.setType("HotelRoom");
        }
    }
}

