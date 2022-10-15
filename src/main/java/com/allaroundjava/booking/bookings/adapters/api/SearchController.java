package com.allaroundjava.booking.bookings.adapters.api;

import com.allaroundjava.booking.bookings.adapters.db.FindAvailabilitiesRepository;
import com.allaroundjava.booking.bookings.application.SearchService;
import com.allaroundjava.booking.bookings.readmodel.RoomDetail;
import com.allaroundjava.booking.bookings.shared.Money;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.OffsetTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;

@RestController
@RequestMapping("/owners")
@AllArgsConstructor
class SearchController {
    FindAvailabilitiesRepository findAvailabilitiesRepository;
    private final SearchService searchService;

    @GetMapping("{ownerId}/availabilities")
    ResponseEntity<FindAvailabilityResponse> findAvailabilities(@PathVariable UUID ownerId,
                                                                @RequestParam Integer capacity,
                                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
                                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo) {
        Collection<RoomDetail> availableRooms = searchService.findAvailableRooms(ownerId, dateFrom, dateTo, capacity);
        return ResponseEntity.ok(FindAvailabilityResponse.from(availableRooms));
    }

    @Value
    static class FindAvailabilityResponse {
        Collection<AvailableRoom> roomsAvailable;

        static FindAvailabilityResponse from(Collection<RoomDetail> avails) {
            Collection<AvailableRoom> result = new LinkedList<>();
            for (RoomDetail avail : avails) {
                result.add(AvailableRoom.from(avail));
            }

            return new FindAvailabilityResponse(result);
        }
    }

    @Value
    static class AvailableRoom {
        UUID roomId;
        int capacity;
        String name;
        Money price;
        OffsetTime hotelHourStart;
        OffsetTime hotelHourEnd;

        static AvailableRoom from(RoomDetail room) {
            return new AvailableRoom(room.getRoomId(),
                    room.getCapacity(),
                    room.getName(),
                    room.getPrice(),
                    room.getArrivalHour(),
                    room.getDepartureHour());
        }
    }
}
