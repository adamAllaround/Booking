package com.allaroundjava.booking.bookings.adapters.api;

import com.allaroundjava.booking.bookings.domain.command.BookCommand;
import com.allaroundjava.booking.bookings.domain.model.Booking;
import com.allaroundjava.booking.bookings.domain.model.Customer;
import com.allaroundjava.booking.bookings.domain.model.Interval;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
@AllArgsConstructor
class BookingsController {
    private final OccupationFacade occupation;

    @PostMapping()
    ResponseEntity<BookingResponse> addBooking(@RequestBody BookingRequest request) {
        Optional<BookingResponse> result = occupation.save(request.toCommand());
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

@Data
class BookingRequest {
    UUID basketId;
    String firstName;
    String lastName;
    String email;
    String phone;

    BookCommand toCommand() {
        return new BookCommand(basketId, new Customer(firstName, lastName, email, phone));
    }
}

