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
class BookingsController {
    private final BookingsFacade bookingsFacade;
    private final OccupationFacade occupation;

    @GetMapping("/{itemId}/bookings")
    ResponseEntity<BookingsResponse> getBookings(@PathVariable UUID itemId) {
        return ResponseEntity.ok(bookingsFacade.getAllByItemId(itemId));
    }

    @PostMapping("/{itemId}/bookings")
    ResponseEntity<BookingResponse> addBooking(@RequestBody BookingRequest request, @PathVariable UUID itemId) {
        request.setItemId(itemId);
        Optional<BookingResponse> result = occupation.saveBooking(request);
        return result.map(resp -> ResponseEntity.created(getUri(resp)).body(resp))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    private URI getUri(BookingResponse resp) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(resp.getId())
                .toUri();
    }
}
