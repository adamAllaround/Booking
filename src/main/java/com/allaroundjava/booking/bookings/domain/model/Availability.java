package com.allaroundjava.booking.bookings.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Availability {
    @EqualsAndHashCode.Include
    UUID id;
    UUID itemId;
    Instant start;
    Instant end;

    public static Availability between(LocalDateTime start, LocalDateTime end) {
        return new Availability(UUID.randomUUID(), start, end);
    }

    static Availability from(Interval interval) {
        return new Availability(UUID.randomUUID(), interval.getStart(), interval.getEnd());
    }

    static Availability from(Booking booking) {
        return new Availability(booking.getId(), booking.getStart(), booking.getEnd());
    }

    boolean overlaps(Availability candidate) {
        return !(end.isBefore(candidate.start) || start.isAfter(candidate.end));
    }

    boolean covers(Interval interval) {
        return start.equals(interval.getStart()) && end.equals(interval.getEnd());
    }

    boolean overlaps(Interval candidate) {
        return !(end.isBefore(candidate.getStart()) || start.isAfter(candidate.getEnd()));
    }
}
