package com.allaroundjava.booking.bookings.domain.model;

import lombok.Getter;
import lombok.NonNull;

import java.time.Instant;

@Getter
public class Interval {
    private final Instant start;
    private final Instant end;

    public Interval(@NonNull Instant start, @NonNull Instant end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Invalid interval. Start Date must be before End Date");
        }
        this.start = start;
        this.end = end;
    }

    boolean overlaps(Interval interval) {
        return !(end.isBefore(interval.start) || start.isAfter(interval.end));
    }

    boolean covers(Interval interval) {
        return start.equals(interval.getStart()) && end.equals(interval.getEnd());
    }
}
