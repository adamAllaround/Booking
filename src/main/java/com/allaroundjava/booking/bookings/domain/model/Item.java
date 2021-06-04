package com.allaroundjava.booking.bookings.domain.model;

import lombok.Value;

import java.time.OffsetTime;
import java.util.UUID;

@Value
public class Item {
    UUID id;
    ItemType itemType;
    OffsetTime hotelHourStart;
    OffsetTime hotelHourEnd;
}
