package com.allaroundjava.booking.bookings.domain.model;

import lombok.AllArgsConstructor;

import java.time.Instant;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public abstract class Availabilities {
    protected final UUID itemId;
    protected final TreeSet<Availability> availabilities;

    public static Availabilities from(Item item, List<Availability> availabilities) {
        return new HotelAvailabilities(item.getId(), new TreeSet<>(availabilities), item.getHotelHourStart(), item.getHotelHourEnd());
    }

    abstract Optional<List<Availability>> tryAdd(Interval interval);

    abstract boolean isContinuous();

    abstract Availabilities bookAll(UUID id);

    boolean remove(Availability availability) {
        return availabilities.remove(availability);
    }

    abstract Availabilities matchingIds(Set<UUID> availabilityIds);

    void addAll(Set<BookedAvailability> bookedAvailabilities) {
        availabilities.addAll(bookedAvailabilities);
    }

    boolean isEmpty() {
        return availabilities.isEmpty();
    }

    boolean isAnyBooked() {
        return availabilities.stream().anyMatch(Availability::isBooked);
    }

    void removeAll(Availabilities toRemove) {
        availabilities.removeAll(toRemove.availabilities);
    }

    void addAll(Availabilities booked) {
        availabilities.addAll(booked.availabilities);
    }
}

