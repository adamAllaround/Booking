package com.allaroundjava.booking.application;

import com.allaroundjava.booking.common.Interval;
import com.allaroundjava.booking.common.Money;
import com.allaroundjava.booking.domain.pricing.QueryForPrice;
import com.allaroundjava.booking.domain.pricing.RoomPrices;
import com.allaroundjava.booking.domain.readmodel.AvailabilitySearch;
import com.allaroundjava.booking.domain.readmodel.RoomDetail;
import com.allaroundjava.booking.domain.readmodel.RoomMeta;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

//this maybe should go to adapters api layer. not much of an application service this is
@RequiredArgsConstructor
public class SearchService {
    private final AvailabilitySearch availabilitySearch;
    private final RoomMeta roomMeta;
    private final QueryForPrice pricing;


    public Collection<RoomDetail> findAvailableRooms(UUID ownerId, LocalDate dateFrom, LocalDate dateTo, Integer capacity) {
        Interval searchInterval = new Interval(dateFrom.atStartOfDay().toInstant(ZoneOffset.UTC),
                dateTo.atTime(23, 59).toInstant(ZoneOffset.UTC));

        Set<UUID> availableRoomIds = availabilitySearch.findAvailableRoomsIn(ownerId, searchInterval);

        if(availableRoomIds.isEmpty()) {
            return Collections.emptySet();
        }

        Set<RoomDetail> roomDetails = roomMeta.find(availableRoomIds, capacity);

        if (roomDetails.isEmpty()) {
            return Collections.emptySet();
        }

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

    private Set<UUID> roomIds(Set<RoomDetail> roomDetails) {
        return roomDetails.stream()
                .map(RoomDetail::getRoomId)
                .collect(Collectors.toSet());
    }
}