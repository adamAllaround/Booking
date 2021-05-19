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
    private final AvailabilitiesFacade availabilities;
    private final OccupationFacade occupation;

    @GetMapping("/{itemId}/availabilities")
    ResponseEntity<AvailabilitiesResponse> getAvailabilities(@PathVariable UUID itemId) {
        return ResponseEntity.ok(availabilities.getAllByItemId(itemId));
    }

    @PostMapping("/{itemId}/availabilities")
    ResponseEntity<AvailabilityResponse> addAvailability(@PathVariable UUID itemId, @RequestBody AvailabilityRequest request) {
        Optional<AvailabilityResponse> response = occupation.save(itemId, request);
        return response.map(resp -> ResponseEntity.created(getUri(resp)).body(resp))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    private URI getUri(AvailabilityResponse response) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
    }
}
