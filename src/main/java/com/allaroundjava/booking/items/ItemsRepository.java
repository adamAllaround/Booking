package com.allaroundjava.booking.items;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
class ItemsRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    List<Item> getAllByOwnerId(UUID ownerId) {
        ImmutableMap<String, UUID> params = ImmutableMap.of("ownerId", ownerId);
        return jdbcTemplate.query("SELECT i.* from Items i where i.ownerId=:ownerId", params, new ItemDatabaseEntity.RowMapper())
                .stream()
                .map(ItemDatabaseEntity::toDomainModel)
                .collect(Collectors.toUnmodifiableList());
    }

    Item save(Item item) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", item.getId());
        params.put("ownerId", item.getOwnerId());
        params.put("name", item.getName());
        params.put("capacity", item.getCapacity());
        params.put("location", item.getLocation());
        params.put("type", item.getType());
        params.put("hotelHourStart", sqlTime(item.getHotelHourStart()));
        params.put("hotelHourEnd", sqlTime(item.getHotelHourEnd()));

        jdbcTemplate.update("INSERT INTO Items (id, ownerId, name, type, capacity, location, hotelHourStart, hotelHourEnd) values (:id, :ownerId, :name, :type, :capacity, :location, :hotelHourStart, :hotelHourEnd)",
                params);
        return item;
    }

    private Time sqlTime(OffsetTime offsetTime) {
        return Optional.ofNullable(offsetTime)
                .map(time -> time.withOffsetSameInstant(ZoneOffset.UTC).toLocalTime())
                .map(Time::valueOf)
                .orElse(null);
    }

    @Data
    static class ItemDatabaseEntity {
        UUID id;
        UUID ownerId;
        String name;
        int capacity;
        String type;
        String location;
        OffsetTime hotelHourStart;
        OffsetTime hotelHourEnd;


        static Item toDomainModel(ItemDatabaseEntity itemDatabaseEntity) {
            Item item = new Item();
            item.setId(itemDatabaseEntity.getId());
            item.setOwnerId(itemDatabaseEntity.getOwnerId());
            item.setName(itemDatabaseEntity.getName());
            item.setLocation(itemDatabaseEntity.getLocation());
            item.setCapacity(itemDatabaseEntity.getCapacity());
            item.setHotelHourStart(itemDatabaseEntity.getHotelHourStart());
            item.setHotelHourEnd(itemDatabaseEntity.getHotelHourEnd());
            item.setType(itemDatabaseEntity.getType());
            return item;
        }

        static class RowMapper implements org.springframework.jdbc.core.RowMapper<ItemDatabaseEntity> {

            @Override
            public ItemDatabaseEntity mapRow(ResultSet resultSet, int i) throws SQLException {
                ItemDatabaseEntity entity = new ItemDatabaseEntity();
                entity.id = UUID.fromString(resultSet.getObject("id").toString());
                entity.ownerId = UUID.fromString(resultSet.getObject("ownerId").toString());
                entity.name = resultSet.getString("name");
                entity.capacity = resultSet.getInt("capacity");
                entity.location = resultSet.getString("location");
                entity.type = resultSet.getString("type");
                entity.location = resultSet.getString("location");
                entity.hotelHourStart = Optional.ofNullable(resultSet.getTime("hotelHourStart")).map(Time::toLocalTime)
                        .map(localTime -> OffsetTime.of(localTime, ZoneOffset.UTC)).orElse(null);
                entity.hotelHourEnd = Optional.ofNullable(resultSet.getTime("hotelHourEnd")).map(Time::toLocalTime)
                        .map(localTime -> OffsetTime.of(localTime, ZoneOffset.UTC)).orElse(null);

                return entity;
            }
        }
    }
}
