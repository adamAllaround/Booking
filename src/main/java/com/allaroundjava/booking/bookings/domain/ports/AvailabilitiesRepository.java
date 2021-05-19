package com.allaroundjava.booking.bookings.domain.ports;

import com.allaroundjava.booking.bookings.domain.model.Availability;

import java.util.List;
import java.util.UUID;

public interface AvailabilitiesRepository {
    List<Availability> getAllByItemId(UUID itemId);
}
