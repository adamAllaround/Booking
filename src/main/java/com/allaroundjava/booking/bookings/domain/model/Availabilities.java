package com.allaroundjava.booking.bookings.domain.model;

import lombok.AllArgsConstructor;

import java.time.Instant;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public abstract class Availabilities {
    protected final List<Availability> availabilities;

    public static Availabilities from(ItemType itemType, OffsetTime hotelHourStart, OffsetTime hotelHourEnd, List<Availability> availabilities) {
        return new HotelAvailabilities(availabilities, hotelHourStart, hotelHourEnd);
    }

    abstract Optional<List<Availability>> tryAdd(Interval interval);

    boolean remove(Availability availability) {
        return availabilities.remove(availability);
    }

    Optional<Availability> findCovering(Interval interval) {
        return availabilities.stream()
                .filter(avail -> avail.covers(interval))
                .findFirst();
    }

}

