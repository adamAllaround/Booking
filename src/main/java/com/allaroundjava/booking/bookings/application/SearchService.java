package com.allaroundjava.booking.bookings.application;

import com.allaroundjava.booking.bookings.readmodel.AvailableRoomsReadModel;
import com.allaroundjava.booking.bookings.readmodel.RoomDetail;
import com.allaroundjava.booking.bookings.shared.Interval;
import com.allaroundjava.booking.bookings.shared.Money;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

//this maybe should go to adapters api layer. not much of an application service this is
@RequiredArgsConstructor
public class SearchService {
    private final AvailableRoomsReadModel rooms;
    private final PricingService pricing;

    public Collection<RoomDetail> findAvailableRooms(UUID ownerId, LocalDate dateFrom, LocalDate dateTo, Integer capacity) {
        Interval searchInterval = new Interval(Instant.from(dateFrom.atStartOfDay()), Instant.from(dateTo.atTime(23, 59)));

        Set<RoomDetail> roomDetails = rooms.find(new RoomSearchQuery(ownerId, searchInterval, capacity));
        RoomPrices roomPrices = pricing.priceFor(roomIds(roomDetails), searchInterval);
        return withPrices(roomDetails, roomPrices);
    }

    private Set<RoomDetail> withPrices(Set<RoomDetail> roomDetails, RoomPrices roomPrices) {
        return roomDetails.stream()
                .map(room -> room.withPrice(priceOrThrow(roomPrices, room)))
                .collect(Collectors.toSet());
    }

    private Money priceOrThrow(RoomPrices roomPrices, RoomDetail room) {
        return roomPrices.of(room.getRoomId()).orElseThrow(() -> new IllegalStateException(String.format("Could not find price for room %s", room.getRoomId())));
    }

    private Collection<UUID> roomIds(Set<RoomDetail> roomDetails) {
        return roomDetails.stream()
                .map(RoomDetail::getRoomId)
                .collect(Collectors.toSet());
    }
}
