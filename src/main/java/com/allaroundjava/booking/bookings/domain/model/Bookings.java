package com.allaroundjava.booking.bookings.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Bookings {
    private final List<Booking> bookings;

    public static Bookings empty() {
        return new Bookings(new ArrayList<>());
    }
}
