package com.allaroundjava.booking.bookings.adapters.db;

import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.Instant;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

@Data
class RoomDetailsDatabaseEntity {
    UUID id;
    Instant created;
    OffsetTime hotelHourStart;
    OffsetTime hotelHourEnd;

//    RoomDetails toModel() {
//        return new RoomDetails(this.id, this.type, this.hotelHourStart, this.hotelHourEnd);
//    }

    static class RowMapper implements org.springframework.jdbc.core.RowMapper<RoomDetailsDatabaseEntity> {

        @Override
        public RoomDetailsDatabaseEntity mapRow(ResultSet resultSet, int i) throws SQLException {
            RoomDetailsDatabaseEntity entity = new RoomDetailsDatabaseEntity();
            entity.id = UUID.fromString(resultSet.getObject("id").toString());
            entity.created = resultSet.getTimestamp("created").toInstant();
            entity.hotelHourStart = Optional.ofNullable(resultSet.getTime("hotelHourStart")).map(Time::toLocalTime)
                    .map(localTime -> OffsetTime.of(localTime, ZoneOffset.UTC)).orElse(null);
            entity.hotelHourEnd = Optional.ofNullable(resultSet.getTime("hotelHourEnd")).map(Time::toLocalTime)
                    .map(localTime -> OffsetTime.of(localTime, ZoneOffset.UTC)).orElse(null);

            return entity;
        }
    }
}
