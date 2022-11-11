package com.allaroundjava.booking.bookings.adapters.api;

import com.allaroundjava.booking.bookings.application.ConfirmingReservation;
import com.allaroundjava.booking.bookings.application.InitializeReservationCommand;
import com.allaroundjava.booking.bookings.application.InitializingReservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/reservations")
@AllArgsConstructor
public class ReservationController {
    private final InitializingReservation initializingReservation;
    private final ConfirmingReservation confirmingReservation;

    @PostMapping
    ResponseEntity<InitializeReservationResponse> initializeReservation(@RequestBody InitializeReservationRequest request) {
        return initializingReservation.initialize(new InitializeReservationCommand(request.getRoomId(), request.getDateFrom(), request.getDateTo(), request.getCapacity()))
                .map(InitializeReservationResponse::of)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping(path = "/{reservationId}")
    ResponseEntity<Object> confirmReservation(@PathVariable UUID reservationId) {
        return confirmingReservation.confirm(reservationId)
                .map(success -> ResponseEntity.accepted().build())
                .getOrElseGet(failure -> ResponseEntity.badRequest().body(failure.getReason()));
    }
}

@Data(staticConstructor = "of")
class InitializeReservationResponse {
    private final UUID reservationId;
}

@Data
class InitializeReservationRequest {
    UUID roomId;
    LocalDate dateFrom;
    LocalDate dateTo;
    int capacity;
}
