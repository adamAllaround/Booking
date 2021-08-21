package com.allaroundjava.booking.notifications.items;

import lombok.Value;

import java.time.OffsetTime;
import java.util.UUID;

@Value
public class HotelRoom {
    UUID id;
    UUID ownerId;
    OffsetTime hotelHourStart;
    OffsetTime hotelHourEnd;
}
