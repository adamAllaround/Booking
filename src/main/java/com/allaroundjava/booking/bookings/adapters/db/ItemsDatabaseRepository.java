package com.allaroundjava.booking.bookings.adapters.db;

import com.allaroundjava.booking.bookings.domain.model.Item;
import com.allaroundjava.booking.bookings.domain.model.ItemType;
import com.allaroundjava.booking.bookings.domain.ports.ItemsRepository;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetTime;
import java.time.ZoneOffset;
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
        params.put("created", Timestamp.from(created));
        params.put("type", itemType.name());
        params.put("hotelHourStart", Time.valueOf(hotelHourStart.withOffsetSameInstant(ZoneOffset.UTC).toLocalTime()));
        params.put("hotelHourEnd", Time.valueOf(hotelHourEnd.withOffsetSameInstant(ZoneOffset.UTC).toLocalTime()));

        jdbcTemplate.update("INSERT INTO OccupationItems (id, created, type, hotelHourStart, hotelHourEnd) values (:itemId, :created, :type, :hotelHourStart, :hotelHourEnd)", params);
    }

    @Override
    public Optional<Item> findById(UUID itemId) {
        ImmutableMap<String, Object> params = ImmutableMap.of("itemId", itemId);

        try {
            ItemDatabaseEntity itemDatabaseEntity = jdbcTemplate.queryForObject("SELECT i.* FROM OccupationItems i where id=:itemId",
                    params, new ItemDatabaseEntity.RowMapper());

            return Optional.ofNullable(itemDatabaseEntity)
                    .map(ItemDatabaseEntity::toModel);

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }
}
