package com.allaroundjava.booking.bookings.domain.availability;

import com.allaroundjava.booking.common.Entity;

import java.time.LocalDate;
import java.util.UUID;

public class Reservation extends Entity {
    private final LocalDate from;
    private final LocalDate to;

    Reservation(UUID id, LocalDate from, LocalDate to) {
        super(id);
        this.from = from;
        this.to = to;
    }
}

//rezerwacja bedzie wypluwana przez availability book(w datach)


