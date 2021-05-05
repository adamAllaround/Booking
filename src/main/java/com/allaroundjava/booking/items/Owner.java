package com.allaroundjava.booking.items;

import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
class Owner {
    UUID id;
    Instant created;
}
