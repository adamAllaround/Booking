package com.allaroundjava.booking.bookings.adapters.api;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

class BookingsFacade {
    BookingsResponse getAllByItemId(UUID itemId) {
        return new BookingsResponse(new ArrayList<>());
    }

    Optional<BookingResponse> save(UUID itemId, BookingRequest request) {
        return Optional.empty();
    }
}
