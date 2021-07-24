package com.allaroundjava.booking.common.events;

import lombok.Value;

import java.time.Instant;
import java.time.OffsetTime;
import java.util.UUID;

@Value
public class HotelRoomCreatedEvent extends ItemCreatedEvent {
    OffsetTime hotelHourStart;
    OffsetTime hotelHourEnd;

    HotelRoomCreatedEvent(UUID eventId, Instant created, UUID subjectId, OffsetTime hotelHourStart, OffsetTime hotelHourEnd) {
        super(eventId, created, subjectId);
        this.hotelHourStart = hotelHourStart;
        this.hotelHourEnd = hotelHourEnd;
    }

    public static ItemCreatedEvent now(UUID subjectId, OffsetTime hotelHourStart, OffsetTime hotelHourEnd) {
        return new HotelRoomCreatedEvent(UUID.randomUUID(), Instant.now(), subjectId, hotelHourStart, hotelHourEnd);
    }
}

