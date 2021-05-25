package com.allaroundjava.booking.items;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.time.OffsetTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
class ItemsRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    List<Item> getAllByOwnerId(UUID ownerId) {
        ImmutableMap<String, UUID> params = ImmutableMap.of("ownerId", ownerId);
        return jdbcTemplate.query("SELECT i.* from Items i where i.ownerId=:ownerId", params, new BeanPropertyRowMapper<>(ItemDatabaseEntity.class))
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
        params.put("hotelHourStart", item.getHotelHourStart());
        params.put("hotelHourEnd", item.getHotelHourEnd());
        jdbcTemplate.update("INSERT INTO Items (id, ownerId, name, type, capacity, location, hotelHourStart, hotelHourEnd) values (:id, :ownerId, :name, :type, :capacity, :location, :hotelHourStart, :hotelHourEnd)",
                params);
        return item;
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
    }
}
