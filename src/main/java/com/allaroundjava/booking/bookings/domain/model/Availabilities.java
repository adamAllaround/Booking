package com.allaroundjava.booking.bookings.domain.model;

import lombok.AllArgsConstructor;

import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@AllArgsConstructor
public abstract class Availabilities {
    private static final OffsetTime STANDARD_HOTEL_START = OffsetTime.of(15, 0, 0, 0, ZoneOffset.UTC);
    private static final OffsetTime STANDARD_HOTEL_END = OffsetTime.of(10, 0, 0, 0, ZoneOffset.UTC);
    protected final List<Availability> availabilities;

    public static Availabilities empty() {
        return from(ItemType.HotelRoom,
                STANDARD_HOTEL_START,
                STANDARD_HOTEL_END,
                new ArrayList<>());
    }

    public static Availabilities from(ItemType itemType, OffsetTime hotelHourStart, OffsetTime hotelHourEnd, List<Availability> availabilities) {
        return new HotelAvailabilities(availabilities, hotelHourStart, hotelHourEnd);
    }

    abstract void add(Availability availability);

    abstract boolean overlapsExisting(Interval candidate);

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

class HotelAvailabilities extends Availabilities{

    public HotelAvailabilities(List<Availability> availabilities, OffsetTime hotelHourStart, OffsetTime hotelHourEnd) {
        super(availabilities);
    }

    @Override
    void add(Availability availability) {
        availabilities.add(availability);

    }

    @Override
    boolean overlapsExisting(Interval candidate) {
        return availabilities.stream()
                .anyMatch(avail -> avail.overlaps(candidate));
    }
}
