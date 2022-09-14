package com.allaroundjava.booking.bookings.application;

import com.allaroundjava.booking.bookings.shared.Money;
import lombok.Value;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Value
public class RoomPrices {
    Map<UUID, Money> roomPrices;

    public Optional<Money> of(UUID roomId) {
        return Optional.ofNullable(roomPrices.get(roomId));
    }
}