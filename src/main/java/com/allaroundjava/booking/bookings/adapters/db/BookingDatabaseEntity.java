package com.allaroundjava.booking.bookings.adapters.db;

import com.allaroundjava.booking.bookings.domain.model.Booking;
import com.allaroundjava.booking.bookings.domain.model.Interval;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.UUID;

@Data
public class BookingDatabaseEntity {
    UUID id;
    UUID itemId;
    OffsetDateTime startTime;
    OffsetDateTime endTime;

    Booking toModel() {
        return new Booking(id, itemId, new Interval(startTime.toInstant(), endTime.toInstant()), new HashSet<>());
    }

    static class RowMapper implements org.springframework.jdbc.core.RowMapper<BookingDatabaseEntity> {

        @Override
        public BookingDatabaseEntity mapRow(ResultSet resultSet, int i) throws SQLException {
            BookingDatabaseEntity entity = new BookingDatabaseEntity();
            entity.id = UUID.fromString(resultSet.getObject("id").toString());
            entity.itemId = UUID.fromString(resultSet.getObject("itemId").toString());
            entity.startTime = resultSet.getTimestamp("startTime").toInstant().atOffset(ZoneOffset.UTC);
            entity.endTime = resultSet.getTimestamp("endTime").toInstant().atOffset(ZoneOffset.UTC);

            return entity;
        }
    }
}
