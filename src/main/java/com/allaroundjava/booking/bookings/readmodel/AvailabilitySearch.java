package com.allaroundjava.booking.bookings.readmodel;

import com.allaroundjava.booking.bookings.shared.Interval;

import java.util.Set;
import java.util.UUID;

public interface AvailabilitySearch {
    Set<UUID> findAvailableRoomsIn(UUID ownerId, Interval interval);
}
