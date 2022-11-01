package com.allaroundjava.booking.bookings.application;

import com.allaroundjava.booking.bookings.details.CustomerDetails;
import com.allaroundjava.booking.bookings.details.ReservationDetails;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class AddingCustomerDetails {
    private final ReservationDetails reservationDetails;

    public void addCustomerDetails(UUID reservationId, CustomerDetails customerDetails) {
        reservationDetails.addCustomerDetails(reservationId, customerDetails);
    }
}
