package com.allaroundjava.booking.bookings.application;

import lombok.Value;

import java.time.LocalDate;
import java.util.UUID;

@Value
public class InitializeReservationCommand {
    UUID roomId;
    LocalDate dateFrom;
    LocalDate dateTo;
    int guests;
}
