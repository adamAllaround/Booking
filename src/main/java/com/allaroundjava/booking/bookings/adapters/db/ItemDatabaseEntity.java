package com.allaroundjava.booking.bookings.adapters.db;

import com.allaroundjava.booking.bookings.domain.model.Item;
import com.allaroundjava.booking.bookings.domain.model.ItemType;
import lombok.Data;

import java.time.Instant;
import java.time.OffsetTime;
import java.util.UUID;

@Data
class ItemDatabaseEntity {
    UUID id;
    Instant created;
    ItemType itemType;
    OffsetTime hotelHourStart;
    OffsetTime hotelHourEnd;

    Item toModel() {
        return new Item(this.id, this.itemType, this.hotelHourStart, this.hotelHourEnd);
    }
}
