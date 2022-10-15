package com.allaroundjava.booking.bookings.domain.model.availability;

import lombok.Value;

import java.util.Set;

@Value
class Reservations {
    Set<Reservation> reservationSet;
}
