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
        return jdbcTemplate.query("SELECT i.* from Items i where i.ownerId=:ownerId",
                params,
                new BeanPropertyRowMapper<>(ItemDatabaseEntity.class))
                .stream()
                .map(ItemDatabaseEntity::toDomainModel)
                .collect(Collectors.toUnmodifiableList());
    }

    @Data
    static class ItemDatabaseEntity {
        UUID id;
        String name;
        int capacity;
        String location;

        static Item toDomainModel(ItemDatabaseEntity itemDatabaseEntity) {
            return new Item(itemDatabaseEntity.getId(),
                    itemDatabaseEntity.getName(),
                    itemDatabaseEntity.getCapacity(),
                    itemDatabaseEntity.getLocation());
        }
    }
}
