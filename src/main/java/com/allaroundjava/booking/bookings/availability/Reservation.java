package com.allaroundjava.booking.bookings.availability;

import com.allaroundjava.booking.common.Entity;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
class Reservation extends Entity {
    private final UUID roomId;
    private final LocalDate from;
    private final LocalDate to;

    Reservation(UUID id, UUID roomId, LocalDate from, LocalDate to) {
        super(id);
        this.roomId = roomId;
        this.from = from;
        this.to = to;
    }
}