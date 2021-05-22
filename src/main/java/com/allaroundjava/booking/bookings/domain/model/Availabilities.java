package com.allaroundjava.booking.bookings.domain.model;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

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

    public <T> Stream<T> map(Function<? super Availability, T> mapper) {
        return availabilities.stream().map(mapper);
    }
}
