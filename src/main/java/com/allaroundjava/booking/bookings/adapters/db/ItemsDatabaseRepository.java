package com.allaroundjava.booking.bookings.adapters.db;

import com.allaroundjava.booking.bookings.domain.model.Item;
import com.allaroundjava.booking.bookings.domain.ports.ItemsRepository;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ItemsDatabaseRepository implements ItemsRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    @Override
    public void saveNew(UUID itemId, Instant created) {
        ImmutableMap<String, Object> params = ImmutableMap.of("itemId", itemId,
                "created", created);
        jdbcTemplate.update("INSERT INTO OccupationItems (id, created) values (:itemId, :created)", params);
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
