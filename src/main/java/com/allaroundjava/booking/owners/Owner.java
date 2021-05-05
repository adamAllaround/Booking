package com.allaroundjava.booking.owners;

import lombok.Data;

import java.util.UUID;

@Data
class Owner {
    UUID id = UUID.randomUUID();
    String name;
    String contact;
}
