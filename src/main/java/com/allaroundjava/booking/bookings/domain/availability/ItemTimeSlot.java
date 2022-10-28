package com.allaroundjava.booking.bookings.domain.availability;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

//aggregate root
@RequiredArgsConstructor
public class ItemTimeSlot {
    private final Set<UUID> reservationIds;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;

    public boolean isAvailable() {
        return reservationIds.isEmpty();
    }

    public Reservation book(UUID reservationId) {
        if(!isAvailable()) {
            throw new IllegalStateException("Trying to book already occupied slot");
        }
        return new Reservation(reservationId, dateFrom, dateTo);
    }
}
