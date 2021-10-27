package com.allaroundjava.booking.bookings.domain.ports;

import com.allaroundjava.booking.bookings.domain.model.Booking;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class BookingsService { //TODO remove - this is a read model
    private final BookingsRepository bookingsRepository;
    public List<Booking> getAllByItemId(UUID itemId) {
        return bookingsRepository.getAllByItemId(itemId);
    }
}
