package com.allaroundjava.booking.bookings.adapters.api;

import com.allaroundjava.booking.bookings.application.InitializeReservationCommand;
import com.allaroundjava.booking.bookings.application.InitializingReservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/reservations")
@AllArgsConstructor
public class ReservationController {
    private final InitializingReservation initializingReservation;
    @PostMapping
    ResponseEntity<InitializeReservationResponse> initializeReservation(@RequestBody InitializeReservationRequest request) {
        return initializingReservation.initialize(new InitializeReservationCommand(request.getRoomId(), request.getDateFrom(), request.getDateTo(), request.getCapacity()))
                .map(InitializeReservationResponse::of)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
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
