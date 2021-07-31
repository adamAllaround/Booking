package com.allaroundjava.booking.bookings.adapters.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
class BookingResponse {
    UUID id;

    static BookingResponse withBookingId(UUID bookingId) {
        return new BookingResponse(bookingId);
    }
}
