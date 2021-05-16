package com.allaroundjava.booking.bookings.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class Availabilities {
    private final List<Availability> availabilities;

    public static Availabilities empty() {
        return new Availabilities(new ArrayList<>());
    }

    void add(Availability availability) {
        availabilities.add(availability);
    }

    boolean overlapsExisting(Interval candidate) {
        return availabilities.stream()
                .anyMatch(avail -> avail.overlaps(candidate));
    }

    boolean remove(Availability availability) {
        return availabilities.remove(availability);
    }

    Optional<Availability> findCovering(Interval interval) {
        return availabilities.stream()
                .filter(avail -> avail.covers(interval))
                .findFirst();
    }
}
