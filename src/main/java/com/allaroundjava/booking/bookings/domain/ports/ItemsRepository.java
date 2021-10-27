package com.allaroundjava.booking.bookings.domain.ports;

import com.allaroundjava.booking.bookings.domain.model.RoomDetails;
import com.allaroundjava.booking.bookings.domain.model.ItemType;

import java.time.Instant;
import java.time.OffsetTime;
import java.util.Optional;
import java.util.UUID;

public interface ItemsRepository {
//    Optional<RoomDetails> findById(UUID itemId);

    void saveNew(UUID itemId, Instant created, ItemType itemType, OffsetTime hotelHourStart, OffsetTime hotelHourEnd);
}
