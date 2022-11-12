package com.allaroundjava.booking.adapters.api;

import com.allaroundjava.booking.application.AddingPaymentDetails;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class AddPaymentDetailsController {
    private final AddingPaymentDetails addingPaymentDetails;

    @PostMapping(path = "{reservationId}/payments")
    ResponseEntity<Void> addPaymentDetails(@PathVariable UUID reservationId, @RequestBody AddPaymentDetailsRequest request) {
        addingPaymentDetails.addPaymentDetails(reservationId, request.getPaymentType());
        return ResponseEntity.noContent().build();
    }
}

@Data
class AddPaymentDetailsRequest {
    String paymentType;
}
