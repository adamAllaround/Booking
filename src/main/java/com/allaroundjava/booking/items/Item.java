package com.allaroundjava.booking.items;

import lombok.Value;

import java.util.UUID;

@Value
public class Item {
    UUID uuid;
    UUID ownerId;
    String name;
    int capacity;
    String location;
}
