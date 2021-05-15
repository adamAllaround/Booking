package com.allaroundjava.booking.bookings.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
class Availability {
    @EqualsAndHashCode.Include
    UUID id;
    LocalDateTime start;
    LocalDateTime end;

    public static Availability between(LocalDateTime start, LocalDateTime end) {
        return new Availability(UUID.randomUUID(), start, end);
    }

    static Availability from(Interval interval) {
        return new Availability(UUID.randomUUID(), interval.getStart(), interval.getEnd());
    }

    boolean overlaps(Availability candidate) {
        return !(end.isBefore(candidate.start) || start.isAfter(candidate.end));
    }
}
