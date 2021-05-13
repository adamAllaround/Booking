package com.allaroundjava.booking.items;

import lombok.Data;

import java.util.UUID;

@Data
class Item {
    UUID id = UUID.randomUUID();
    UUID ownerId;
    String name;
    int capacity;
    String location;
}
