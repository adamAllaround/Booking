package com.allaroundjava.booking.notifications.owners;

import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class Owner {
    UUID id;
    String email;
}
