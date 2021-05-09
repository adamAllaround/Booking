package com.allaroundjava.booking.items;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
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
        ImmutableMap<String, Object> params = ImmutableMap.of("id", item.getUuid(),
                "ownerId", item.getOwnerId(),
                "name", item.getName(),
                "capacity", item.getCapacity(),
                "location", item.getLocation());
        jdbcTemplate.update("INSERT INTO Items (id, ownerId, name, capacity, location) values (:id, :ownerID, :name, :capacity, :location)",
                params);
        return item;
    }

    @Data
    static class ItemDatabaseEntity {
        UUID id;
        UUID ownerId;
        String name;
        int capacity;
        String location;

        static Item toDomainModel(ItemDatabaseEntity itemDatabaseEntity) {
            return new Item(itemDatabaseEntity.getId(),
                    itemDatabaseEntity.getOwnerId(),
                    itemDatabaseEntity.getName(),
                    itemDatabaseEntity.getCapacity(),
                    itemDatabaseEntity.getLocation());
        }
    }
}
