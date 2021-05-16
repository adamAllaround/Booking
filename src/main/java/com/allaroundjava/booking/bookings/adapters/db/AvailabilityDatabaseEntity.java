package com.allaroundjava.booking.bookings.adapters.db;

import com.allaroundjava.booking.bookings.domain.model.Availability;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.OffsetDateTime;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
class AvailabilityDatabaseEntity {
    @Id
    Long id;
    UUID availabilityId;
    UUID itemId;
    OffsetDateTime start;
    OffsetDateTime end;

    AvailabilityDatabaseEntity(UUID itemId, UUID availabilityId, OffsetDateTime start, OffsetDateTime end) {
        this.itemId = itemId;
        this.availabilityId = availabilityId;
        this.start = start;
        this.end = end;
    }

    Availability toModel() {
        return new Availability(availabilityId, itemId, start.toInstant(), end.toInstant());
    }
}
