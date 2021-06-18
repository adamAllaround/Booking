package com.allaroundjava.booking.bookings.domain.model;

import java.time.Instant;

public class BookingCalendar {
    Instant now() {
        return Instant.now();
    }
}
