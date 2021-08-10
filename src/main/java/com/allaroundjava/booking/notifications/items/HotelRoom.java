package com.allaroundjava.booking.notifications.items;

import lombok.Value;

import java.time.OffsetTime;
import java.util.UUID;

@Value
public class HotelRoom {
    UUID id;
    OffsetTime hotelHourStart;
    OffsetTime hotelHourEnd;
}
