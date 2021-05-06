package com.allaroundjava.booking.items;

import lombok.Value;

import java.util.UUID;

@Value
public class Item {
    UUID uuid;
    String name;
    int capacity;
    String location;
}
