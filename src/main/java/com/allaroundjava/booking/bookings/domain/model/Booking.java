package com.allaroundjava.booking.bookings.domain.model;

import lombok.Value;

import java.time.Instant;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Value
public class Booking {
    UUID id;
    UUID itemId;
    Interval interval;
    Set<UUID> availabilityIds;

    public Instant getStart() {
        return interval.getStart();
    }

    public Instant getEnd() {
        return interval.getEnd();
    }
}
