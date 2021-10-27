package com.allaroundjava.booking.bookings.domain.model;

import com.allaroundjava.booking.common.ValueObject;
import lombok.Value;

import java.time.OffsetTime;
import java.util.UUID;

@Value
public class RoomDetails extends ValueObject {
    UUID id;
    OffsetTime hotelHourStart;
    OffsetTime hotelHourEnd;
}
