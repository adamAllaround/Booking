package com.allaroundjava.booking.bookings.adapters.api;

import com.allaroundjava.booking.bookings.adapters.db.FindAvailabilitiesRepository;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.OffsetTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/owners")
@AllArgsConstructor
public class SearchController {
    FindAvailabilitiesRepository findAvailabilitiesRepository;

    @GetMapping("{ownerId}/availabilities")
    ResponseEntity<FindAvailabilityResponse> findAvailabilities(@PathVariable UUID ownerId,
                                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
                                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo) {
        List<FindAvailabilitiesRepository.Avail> avails = findAvailabilitiesRepository.find(ownerId, dateFrom, dateTo);
        return ResponseEntity.ok(FindAvailabilityResponse.from(avails));
    }

    @Value
    static class FindAvailabilityResponse {
        Collection<ToBook> roomsAvailable;

        static FindAvailabilityResponse from(List<FindAvailabilitiesRepository.Avail> avails) {
            Collection<ToBook> result = new LinkedList<>();
            for (FindAvailabilitiesRepository.Avail avail : avails) {
                result.add(ToBook.from(avail));
            }

            return new FindAvailabilityResponse(result);
        }
    }

    @Value
    static class ToBook {
        UUID roomId;
        int capacity;
        String name;
        OffsetTime hotelHourStart;
        OffsetTime hotelHourEnd;

        static ToBook from(FindAvailabilitiesRepository.Avail avail) {
            return new ToBook(avail.getRoomId(),
                    avail.getCapacity(),
                    avail.getName(),
                    avail.getHotelHourStart(),
                    avail.getHotelHourEnd());
        }
    }
}
