package com.allaroundjava.booking.bookings.domain.model;

import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

@Getter
class Interval {
    private final LocalDateTime start;
    private final LocalDateTime end;

    Interval(@NonNull LocalDateTime start,@NonNull LocalDateTime end) {
        if(start.isAfter(end)) {
            throw new IllegalArgumentException("Invalid interval. Start Date must be before End Date");
        }
        this.start = start;
        this.end = end;
    }
}
