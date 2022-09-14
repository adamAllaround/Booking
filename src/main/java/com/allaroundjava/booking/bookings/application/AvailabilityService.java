package com.allaroundjava.booking.bookings.application;

import com.allaroundjava.booking.bookings.shared.Interval;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public class AvailabilityService {
    Set<UUID> findAvailableRoomsIn(UUID ownerId, Interval interval) {
        return Collections.emptySet();
    }
}
