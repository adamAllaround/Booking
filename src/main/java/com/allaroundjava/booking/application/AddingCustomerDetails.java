package com.allaroundjava.booking.application;

import com.allaroundjava.booking.domain.details.CustomerDetails;
import com.allaroundjava.booking.domain.details.ReservationDetails;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class AddingCustomerDetails {
    private final ReservationDetails reservationDetails;

    public void addCustomerDetails(UUID reservationId, CustomerDetails customerDetails) {
        reservationDetails.addCustomerDetails(reservationId, customerDetails);
    }
}
