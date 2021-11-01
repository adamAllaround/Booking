package com.allaroundjava.booking.bookings.adapters.db;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class RoomsDatabaseRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public RoomDatabaseEntity save(RoomDatabaseEntity room) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", room.getId());
        params.put("ownerId", room.getOwnerId());
        params.put("name", room.getName());
        params.put("capacity", room.getCapacity());
        params.put("location", room.getLocation());
        params.put("hotelHourStart", sqlTime(room.getHotelHourStart()));
        params.put("hotelHourEnd", sqlTime(room.getHotelHourEnd()));
        params.put("created", Timestamp.from(room.getCreated()));
        jdbcTemplate.update("INSERT INTO Rooms (id, ownerId, name, capacity, location, hotelHourStart, hotelHourEnd, created) values (:id, :ownerId, :name, :capacity, :location, :hotelHourStart, :hotelHourEnd, :created)",
                params);
        return room;

    }

    private Time sqlTime(OffsetTime offsetTime) {
        return Optional.ofNullable(offsetTime)
                .map(time -> time.withOffsetSameInstant(ZoneOffset.UTC).toLocalTime())
                .map(Time::valueOf)
                .orElse(null);
    }

    @Value
    public static class RoomDatabaseEntity {
        UUID id;
        UUID ownerId;
        String name;
        int capacity;
        String location;
        OffsetTime hotelHourStart;
        OffsetTime hotelHourEnd;
        Instant created;

        public static RoomDatabaseEntity now(UUID ownerId, String name, int capacity, String location, OffsetTime hotelHourStart, OffsetTime hotelHourEnd) {
            return new RoomDatabaseEntity(UUID.randomUUID(), ownerId, name, capacity, location, hotelHourStart, hotelHourEnd, Instant.now());
        }

    }
}
