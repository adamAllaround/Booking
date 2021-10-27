package com.allaroundjava.booking.bookings.domain.model;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class Bookings {
    private final List<Booking> bookings;

    public static Bookings empty() {
        return new Bookings(new ArrayList<>());
    }

    boolean remove(Booking booking) {
        return bookings.remove(booking);
    }
}
