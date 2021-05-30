package com.allaroundjava.booking.bookings.adapters.db;

import com.allaroundjava.booking.bookings.domain.model.Item;
import com.allaroundjava.booking.bookings.domain.model.ItemType;
import com.allaroundjava.booking.bookings.domain.ports.ItemsRepository;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.OffsetTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ItemsDatabaseRepository implements ItemsRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void saveNew(UUID itemId, Instant created, ItemType itemType, OffsetTime hotelHourStart, OffsetTime hotelHourEnd) {
        Map<String, Object> params = new HashMap<>();
        params.put("itemId", itemId);
        params.put("created", created);
        params.put("type", itemType.name());
        params.put("hotelHourStart", hotelHourStart);
        params.put("hotelHourEnd", hotelHourEnd);

        jdbcTemplate.update("INSERT INTO OccupationItems (id, created, type, hotelHourStart, hotelHourEnd) values (:itemId, :created, :type, :hotelHourStart, :hotelHourEnd)", params);
    }

    @Override
    public Optional<Item> findById(UUID itemId) {
        ImmutableMap<String, Object> params = ImmutableMap.of("itemId", itemId);

        ItemDatabaseEntity itemDatabaseEntity = jdbcTemplate.queryForObject("SELECT i.* FROM OccupationItems i where id=:itemId",
                params, new BeanPropertyRowMapper<>(ItemDatabaseEntity.class));
        return Optional.ofNullable(itemDatabaseEntity)
                .map(ItemDatabaseEntity::toModel);
    }
}
