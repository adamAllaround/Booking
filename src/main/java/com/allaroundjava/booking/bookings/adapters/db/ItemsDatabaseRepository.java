package com.allaroundjava.booking.bookings.adapters.db;

import com.allaroundjava.booking.bookings.domain.model.ItemType;
import com.allaroundjava.booking.bookings.domain.ports.ItemsRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ItemsDatabaseRepository implements ItemsRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void saveNew(UUID itemId, Instant created, ItemType itemType, OffsetTime hotelHourStart, OffsetTime hotelHourEnd) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", itemId);
        params.put("created", Timestamp.from(created));
        params.put("hotelHourStart", Time.valueOf(hotelHourStart.withOffsetSameInstant(ZoneOffset.UTC).toLocalTime()));
        params.put("hotelHourEnd", Time.valueOf(hotelHourEnd.withOffsetSameInstant(ZoneOffset.UTC).toLocalTime()));

        jdbcTemplate.update("INSERT INTO OccupationItems (id, created, type, hotelHourStart, hotelHourEnd) values (:itemId, :created, :type, :hotelHourStart, :hotelHourEnd)", params);
    }

//    @Override
//    public Optional<RoomDetails> findById(UUID itemId) {
//        ImmutableMap<String, Object> params = ImmutableMap.of("itemId", itemId);
//
//        try {
//            RoomDetailsDatabaseEntity roomDetailsDatabaseEntity = jdbcTemplate.queryForObject("SELECT i.* FROM OccupationItems i where id=:itemId",
//                    params, new RoomDetailsDatabaseEntity.RowMapper());
//
//            return Optional.ofNullable(roomDetailsDatabaseEntity)
//                    .map(RoomDetailsDatabaseEntity::toModel);
//
//        } catch (EmptyResultDataAccessException e) {
//            return Optional.empty();
//        }
//
//    }
}
