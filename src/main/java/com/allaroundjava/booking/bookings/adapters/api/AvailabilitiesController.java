package com.allaroundjava.booking.bookings.adapters.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
class AvailabilitiesController {
//    private final AvailabilitiesFacade availabilities;
    private final OccupationFacade occupation;

//    @GetMapping("/{itemId}/availabilities")
//    ResponseEntity<AvailabilitiesResponse> getAvailabilities(@PathVariable UUID itemId) {
//        return ResponseEntity.ok(availabilities.getAllByItemId(itemId));
//    }

    @PostMapping("/{itemId}/availabilities")
    ResponseEntity<AvailabilitiesResponse> addAvailability(@PathVariable UUID itemId, @RequestBody AvailabilityRequest request) {
        Optional<AvailabilitiesResponse> response = occupation.save(itemId, request);
        return response.map(resp -> ResponseEntity.created(getUri()).body(resp))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    private URI getUri() {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build()
                .toUri();
//        This is not quite according to HTTP standard as we're not returning a location of created resource
//        but since we're creating multiple we're accepting that we're returning items with ids in response
//        together with a path to list all availabilities for given item
    }
}
