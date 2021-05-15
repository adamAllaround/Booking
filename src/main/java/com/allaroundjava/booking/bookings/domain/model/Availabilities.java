package com.allaroundjava.booking.bookings.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Availabilities {
    private final List<Availability> availabilities;

    public static Availabilities empty() {
        return new Availabilities(new ArrayList<>());
    }

    void add(Availability availability) {
        availabilities.add(availability);
    }
}
