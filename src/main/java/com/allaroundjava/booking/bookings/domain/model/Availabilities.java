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
    protected final List<Availability> availabilities;

    public static Availabilities from(Item item, List<Availability> availabilities) {
        return new HotelAvailabilities(item.getId(), availabilities, item.getHotelHourStart(), item.getHotelHourEnd());
    }

    abstract Optional<List<Availability>> tryAdd(Interval interval);

    boolean remove(Availability availability) {
        return availabilities.remove(availability);
    }

    Set<Availability> matchingIds(Set<UUID> availabilityIds) {
        return availabilities
                .stream()
                .filter(availability -> availabilityIds.contains(availability.getId())).collect(Collectors.toUnmodifiableSet());
    }

    void addAll(Set<BookedAvailability> bookedAvailabilities) {
        availabilities.addAll(bookedAvailabilities);
    }
}

