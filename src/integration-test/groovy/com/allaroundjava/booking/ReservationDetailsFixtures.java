package com.allaroundjava.booking;

import com.allaroundjava.booking.domain.details.ReservationDetails;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@RequiredArgsConstructor
public class ReservationDetailsFixtures {
    private final ReservationDetails reservationDetails;

    public void reservationInitialized(UUID reservationId, LocalDate dateFrom, LocalDate dateTo) {
        reservationDetails.initialize(reservationId, UUID.randomUUID(), dateFrom, dateTo, 2);
    }
        
}
