package com.allaroundjava.booking.bookings.domain.model.availability;

import com.allaroundjava.booking.bookings.shared.Interval;
import lombok.Value;

import java.util.UUID;

@Value
class Reservation {
    UUID reservationId;
    Interval interval;
}
