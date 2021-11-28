package com.allaroundjava.booking.bookings.adapters.db;

import com.allaroundjava.booking.bookings.domain.model.Basket;
import com.allaroundjava.booking.bookings.domain.model.Interval;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class BasketDatabaseEntity {
    UUID id;
    UUID roomId;
    OffsetDateTime startTime;
    OffsetDateTime endTime;

    Basket toModel() {
        return new Basket(id, roomId, new Interval(startTime.toInstant(), endTime.toInstant()));
    }

    static class RowMapper implements org.springframework.jdbc.core.RowMapper<BasketDatabaseEntity> {

        @Override
        public BasketDatabaseEntity mapRow(ResultSet resultSet, int i) throws SQLException {
            BasketDatabaseEntity entity = new BasketDatabaseEntity();
            entity.id = UUID.fromString(resultSet.getObject("id").toString());
            entity.roomId = UUID.fromString(resultSet.getObject("roomId").toString());
            entity.startTime = resultSet.getTimestamp("startTime").toInstant().atOffset(ZoneOffset.UTC);
            entity.endTime = resultSet.getTimestamp("endTime").toInstant().atOffset(ZoneOffset.UTC);

            return entity;
        }
    }
}
