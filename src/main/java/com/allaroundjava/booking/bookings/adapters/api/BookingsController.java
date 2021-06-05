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
    @GetMapping("/{itemId}/bookings")
    ResponseEntity<BookingsResponse> getBookings(@PathVariable UUID itemId) {
        return ResponseEntity.ok(bookingsFacade.getAllByItemId(itemId));
    }

    @PostMapping("/{itemId}/bookings")
    ResponseEntity<BookingResponse> addBooking(@PathVariable UUID itemId, @RequestBody BookingRequest request) {
        Optional<BookingResponse> result = bookingsFacade.save(itemId, request);
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
