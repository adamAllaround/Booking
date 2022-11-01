package com.allaroundjava.booking.bookings.adapters.api;

import com.allaroundjava.booking.bookings.application.AddingCustomerDetails;
import com.allaroundjava.booking.bookings.details.CustomerDetails;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
class AddCustomerDetailsController {
    private final AddingCustomerDetails addingCustomerDetails;

    @PostMapping(path = "{reservationId}/customers")
    ResponseEntity<Void> addCustomerDetails(@PathVariable UUID reservationId, @RequestBody AddCustomerDetailsRequest request) {
        addingCustomerDetails.addCustomerDetails(reservationId, new CustomerDetails(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPhoneNumber()));
        return ResponseEntity.noContent().build();
    }
}

@Data
class AddCustomerDetailsRequest {
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
}

