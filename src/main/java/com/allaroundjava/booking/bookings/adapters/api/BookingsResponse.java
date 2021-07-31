package com.allaroundjava.booking.bookings.adapters.api;

import com.allaroundjava.booking.bookings.domain.model.Booking;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
class BookingsResponse {
    Collection<BookingResponse> bookings;

    static BookingsResponse from(List<Booking> bookings) {
        List<BookingResponse> bookingResponses = bookings.stream()
                .map(booking -> BookingResponse.withBookingId(booking.getId())).collect(Collectors.toList());
        return new BookingsResponse(bookingResponses);
    }
}
