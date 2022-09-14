package com.allaroundjava.booking.bookings.adapters.api;

import com.allaroundjava.booking.bookings.adapters.db.RoomsDatabaseRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.OffsetTime;
import java.util.UUID;

//@RestController
//@RequestMapping("/owners")
@AllArgsConstructor
public class AddRoomController {
    private final RoomsDatabaseRepository roomsRepository;

    @PostMapping("/{ownerId}/items")
    ResponseEntity<AddRoomResponse> addRoom(@PathVariable UUID ownerId, @RequestBody AddRoomRequest addRoomRequest) {
        RoomsDatabaseRepository.RoomDatabaseEntity room = roomsRepository.save(addRoomRequest.toEntityWithOwner(ownerId));
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(room.getId()).toUri();
        return ResponseEntity.created(uri)
                .body(AddRoomResponse.from(room));
    }

    @Data
    public static class AddRoomRequest {
        String name;
        int capacity;
        String location;
        OffsetTime hotelHourStart;
        OffsetTime hotelHourEnd;

        RoomsDatabaseRepository.RoomDatabaseEntity toEntityWithOwner(UUID ownerId) {
            return RoomsDatabaseRepository.RoomDatabaseEntity.now(ownerId, name, capacity, location, hotelHourStart, hotelHourEnd);
        }
    }

    @Value
    public static class AddRoomResponse {
        UUID id;
        UUID ownerId;
        String name;
        int capacity;
        String location;
        OffsetTime hotelHourStart;
        OffsetTime hotelHourEnd;

        static AddRoomResponse from(RoomsDatabaseRepository.RoomDatabaseEntity room) {
            return new AddRoomResponse(room.getId(), room.getOwnerId(), room.getName(), room.getCapacity(), room.getLocation(), room.getHotelHourStart(), room.getHotelHourEnd());
        }
    }
}
