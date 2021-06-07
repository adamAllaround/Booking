package com.allaroundjava.booking.bookings.adapters.api;

import com.allaroundjava.booking.bookings.domain.model.Booking;
import com.allaroundjava.booking.bookings.domain.ports.BookingsService;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
class BookingsFacade {
    private final BookingsService bookingsService;
    BookingsResponse getAllByItemId(UUID itemId) {
        List<Booking> bookings = bookingsService.getAllByItemId(itemId);

        return BookingsResponse.from(bookings);
    }
}
