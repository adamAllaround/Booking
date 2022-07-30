package com.allaroundjava.booking.bookings.adapters.db;

import com.allaroundjava.booking.bookings.domain.model.Availability;
import com.allaroundjava.booking.bookings.shared.Interval;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

@Data
@NoArgsConstructor
class AvailabilityDatabaseEntity {
    UUID id;
    UUID itemId;
    OffsetDateTime startTime;
    OffsetDateTime endTime;
    UUID bookingId;

    Availability toModel() {
        Availability availability = new Availability(id, itemId, new Interval(startTime.toInstant(), endTime.toInstant()));
        if (bookingId == null) {
            return availability;
        }
        return availability.book(bookingId);
    }

    static class RowMapper implements org.springframework.jdbc.core.RowMapper<AvailabilityDatabaseEntity> {

        @Override
        public AvailabilityDatabaseEntity mapRow(ResultSet resultSet, int i) throws SQLException {
            AvailabilityDatabaseEntity entity = new AvailabilityDatabaseEntity();
            entity.id = UUID.fromString(resultSet.getObject("id").toString());
            entity.itemId = UUID.fromString(resultSet.getObject("itemId").toString());
            entity.startTime = resultSet.getTimestamp("startTime").toInstant().atOffset(ZoneOffset.UTC);
            entity.endTime = resultSet.getTimestamp("endTime").toInstant().atOffset(ZoneOffset.UTC);
            entity.bookingId = Optional.ofNullable(resultSet.getString("bookingId"))
                    .map(UUID::fromString).orElse(null);

            return entity;
        }
    }
}
