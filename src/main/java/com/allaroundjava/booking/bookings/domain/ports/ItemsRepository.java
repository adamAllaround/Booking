package com.allaroundjava.booking.bookings.domain.ports;

import java.time.Instant;
import java.time.OffsetTime;
import java.util.UUID;

public interface ItemsRepository {
    void saveNew(UUID itemId, Instant created, OffsetTime hotelHourStart, OffsetTime hotelHourEnd);
}
