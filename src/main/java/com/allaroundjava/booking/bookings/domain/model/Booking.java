package com.allaroundjava.booking.bookings.domain.model;

import lombok.Value;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class Booking {
    UUID id;
    Instant start;
    Instant end;

    static Booking from(Availability availability) {
        return new Booking(availability.getId(), availability.getStart(), availability.getEnd());
    }
}
