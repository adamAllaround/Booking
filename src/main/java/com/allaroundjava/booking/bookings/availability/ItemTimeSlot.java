package com.allaroundjava.booking.bookings.availability;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

//aggregate root
@RequiredArgsConstructor
class ItemTimeSlot {
    private final Set<UUID> reservationIds;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;

    boolean isAvailable() {
        return reservationIds.isEmpty();
    }

    Reservation book(UUID reservationId) {
        if(!isAvailable()) {
            throw new IllegalStateException("Trying to book already occupied slot");
        }
        return new Reservation(reservationId, dateFrom, dateTo);
    }
}
