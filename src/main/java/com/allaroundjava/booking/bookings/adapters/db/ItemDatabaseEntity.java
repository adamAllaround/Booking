package com.allaroundjava.booking.bookings.adapters.db;

import com.allaroundjava.booking.bookings.domain.model.Item;
import com.allaroundjava.booking.bookings.domain.model.ItemType;
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
class ItemDatabaseEntity {
    UUID id;
    Instant created;
    ItemType type;
    OffsetTime hotelHourStart;
    OffsetTime hotelHourEnd;

    Item toModel() {
        return new Item(this.id, this.type, this.hotelHourStart, this.hotelHourEnd);
    }

    static class RowMapper implements org.springframework.jdbc.core.RowMapper<ItemDatabaseEntity> {

        @Override
        public ItemDatabaseEntity mapRow(ResultSet resultSet, int i) throws SQLException {
            ItemDatabaseEntity entity = new ItemDatabaseEntity();
            entity.id = UUID.fromString(resultSet.getObject("id").toString());
            entity.created = resultSet.getTimestamp("created").toInstant();
            entity.type = ItemType.valueOf(resultSet.getString("type"));
            entity.hotelHourStart = Optional.ofNullable(resultSet.getTime("hotelHourStart")).map(Time::toLocalTime)
                    .map(localTime -> OffsetTime.of(localTime, ZoneOffset.UTC)).orElse(null);
            entity.hotelHourEnd = Optional.ofNullable(resultSet.getTime("hotelHourEnd")).map(Time::toLocalTime)
                    .map(localTime -> OffsetTime.of(localTime, ZoneOffset.UTC)).orElse(null);

            return entity;
        }
    }
}
