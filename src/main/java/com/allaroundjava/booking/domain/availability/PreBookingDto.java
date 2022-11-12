package com.allaroundjava.booking.domain.availability;

import lombok.Value;

import java.time.LocalDate;
import java.util.UUID;

@Value
public class PreBookingDto {
    UUID reservationId;
    UUID roomId;
    LocalDate dateFrom;
    LocalDate dateTo;
}
