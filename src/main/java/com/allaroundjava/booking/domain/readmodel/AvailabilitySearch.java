package com.allaroundjava.booking.domain.readmodel;

import com.allaroundjava.booking.common.Interval;

import java.util.Set;
import java.util.UUID;

public interface AvailabilitySearch {
    Set<UUID> findAvailableRoomsIn(UUID ownerId, Interval interval);
}
