package com.allaroundjava.booking.bookings.adapters.api;

import com.allaroundjava.booking.bookings.domain.model.Booking;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
class BookingResponse {
    UUID id;

    static BookingResponse from(Booking booking) {
        return new BookingResponse(booking.getId());
    }
}
