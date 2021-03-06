package com.allaroundjava.booking.bookings.domain.ports;

import com.allaroundjava.booking.bookings.domain.model.Booking;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingsRepository {
    List<Booking> getAllByItemId(UUID itemId);

    Optional<Booking> getSingle(UUID bookingId);
}
