package com.allaroundjava.booking.owners;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
class Owner {
    UUID id = UUID.randomUUID();
    @NonNull
    String name;
    @NonNull
    String email;
}
