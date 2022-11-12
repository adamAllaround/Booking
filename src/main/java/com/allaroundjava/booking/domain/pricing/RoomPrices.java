package com.allaroundjava.booking.domain.pricing;

import com.allaroundjava.booking.common.Money;
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