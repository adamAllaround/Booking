package com.allaroundjava.booking.items;

import lombok.Data;

import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.UUID;

@Data
class Item {
    UUID id = UUID.randomUUID();
    UUID ownerId;
    String name;
    int capacity;
    String location;
    OffsetTime hotelHourStart;
    OffsetTime hotelHourEnd;
    private String type;
}
