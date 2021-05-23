package com.allaroundjava.booking.bookings.adapters.db;

import com.allaroundjava.booking.bookings.domain.model.Item;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
class ItemDatabaseEntity {
    UUID id;
    Instant created;

    Item toModel() {
        return new Item(this.id);
    }
}
