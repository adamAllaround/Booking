package com.allaroundjava.booking.bookings.adapters.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@Data
@AllArgsConstructor
class BookingsResponse {
    Collection<BookingResponse> bookings;
}
