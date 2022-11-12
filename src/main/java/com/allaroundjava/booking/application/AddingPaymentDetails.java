package com.allaroundjava.booking.application;

import com.allaroundjava.booking.domain.details.ReservationDetails;
import com.allaroundjava.booking.domain.pricing.PaymentService;
import com.allaroundjava.booking.domain.pricing.PaymentType;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class AddingPaymentDetails {
    private final PaymentService paymentService;
    private final ReservationDetails reservationDetails;

    public void addPaymentDetails(UUID reservationId, String paymentType) {
        PaymentType type = PaymentType.of(paymentType)
                .orElseThrow(IllegalArgumentException::new);
        paymentService.setPayment(reservationId, type);
        reservationDetails.addPaymentDetails(reservationId);
    }
}
