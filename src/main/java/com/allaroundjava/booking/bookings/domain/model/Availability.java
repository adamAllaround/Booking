package com.allaroundjava.booking.bookings.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Availability {
    @EqualsAndHashCode.Include
    UUID id;
    UUID itemId;
    Interval interval;

    public static Availability from(UUID itemId, Interval interval) {
        return new Availability(UUID.randomUUID(),itemId,  interval);
    }

    static Availability from(Booking booking) {
        return new Availability(booking.getId(), booking.getItemId(), booking.getInterval());
    }

    boolean overlaps(Availability candidate) {
        return interval.overlaps(candidate.getInterval());
    }

    boolean covers(Interval interval) {
        return this.interval.covers(interval);
    }

    boolean overlaps(Interval candidate) {
        return interval.overlaps(candidate);
    }

    public Instant getStart() {
        return interval.getStart();
    }

    public Instant getEnd() {
        return interval.getEnd();
    }
}
