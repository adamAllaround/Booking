package com.allaroundjava.booking.common.events;

import lombok.Value;

import java.time.Instant;
import java.time.OffsetTime;
import java.util.UUID;

@Value
public class HotelRoomCreatedEvent extends ItemCreatedEvent {
    OffsetTime hotelHourStart;
    OffsetTime hotelHourEnd;

    HotelRoomCreatedEvent(UUID eventId,UUID ownerId, Instant created, UUID itemId, OffsetTime hotelHourStart, OffsetTime hotelHourEnd) {
        super(eventId, created, itemId, ownerId);
        this.hotelHourStart = hotelHourStart;
        this.hotelHourEnd = hotelHourEnd;
    }

    public static ItemCreatedEvent now(UUID itemId,UUID ownerId, OffsetTime hotelHourStart, OffsetTime hotelHourEnd) {
        return new HotelRoomCreatedEvent(UUID.randomUUID(), ownerId, Instant.now(), itemId, hotelHourStart, hotelHourEnd);
    }
}

