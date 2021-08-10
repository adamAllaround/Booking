package com.allaroundjava.booking.notifications.items;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class ItemsRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    void save(HotelRoom hotelRoom) {
        ImmutableMap<String, Object> params = ImmutableMap.of(
                "id", hotelRoom.getId(),
                "hotelHourStart", Time.valueOf(hotelRoom.getHotelHourStart().withOffsetSameInstant(ZoneOffset.UTC).toLocalTime()),
                "hotelHourEnd", Time.valueOf(hotelRoom.getHotelHourEnd().withOffsetSameInstant(ZoneOffset.UTC).toLocalTime()));

        jdbcTemplate.update("INSERT INTO NotificationsItems (id, hotelHourStart, hotelHourEnd) values (:id, :hotelHourStart, :hotelHourEnd)",
                params);
    }

    public Optional<HotelRoom> findById(UUID id) {
        ImmutableMap<String, Object> params = ImmutableMap.of("id", id);
        try {
            ItemDatabaseEntity itemDatabaseEntity = jdbcTemplate.queryForObject("SELECT * FROM NotificationsItems o where id=:id",
                    params, new ItemDatabaseEntity.RowMapper());
            return Optional.ofNullable(itemDatabaseEntity.toDomainModel());
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Data
    @NoArgsConstructor
    static class ItemDatabaseEntity {
        private UUID id;
        private Time hotelHourStart;
        private Time hotelHourEnd;

        HotelRoom toDomainModel() {
            return new HotelRoom(id,
                    OffsetTime.of(hotelHourStart.toLocalTime(), ZoneOffset.UTC),
                    OffsetTime.of(hotelHourEnd.toLocalTime(), ZoneOffset.UTC));
        }

        static class RowMapper implements org.springframework.jdbc.core.RowMapper<ItemDatabaseEntity> {

            @Override
            public ItemDatabaseEntity mapRow(ResultSet resultSet, int i) throws SQLException {
                ItemDatabaseEntity entity = new ItemDatabaseEntity();
                entity.id = UUID.fromString(resultSet.getObject("id").toString());
                entity.hotelHourStart = resultSet.getTime("hotelHourStart");
                entity.hotelHourEnd = resultSet.getTime("hotelHourEnd");

                return entity;
            }
        }
    }
}
