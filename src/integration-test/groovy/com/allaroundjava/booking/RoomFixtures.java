package com.allaroundjava.booking;

import com.allaroundjava.booking.bookings.adapters.db.RoomsDatabaseRepository;
import com.allaroundjava.booking.bookings.domain.model.Dates2020;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.Timestamp;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class RoomFixtures {

//    private final RoomsDatabaseRepository roomsDatabaseRepository;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void existsRoom(UUID roomId, UUID ownerId) {

        RoomsDatabaseRepository.RoomDatabaseEntity room = new RoomsDatabaseRepository.RoomDatabaseEntity(roomId,
                ownerId,
                "test name",
                3,
                "test location",
                OffsetTime.of(15, 0, 0, 0, ZoneOffset.UTC),
                OffsetTime.of(10, 0, 0, 0, ZoneOffset.UTC),
                Dates2020.may(20).hour(12));
//        roomsDatabaseRepository.save(room);
    }

    public void existsRoom2(UUID roomId, UUID ownerId) {
        Map<String, Object> params = ImmutableMap.of("roomId", roomId,
                "ownerId", ownerId);

        namedParameterJdbcTemplate.update("insert into roommeta (id, ownerid, name, description, capacity, location, arrivalhour, departurehour) " +
                "VALUES (:roomId, :ownerId, 'Szarotka', 'Opis pokoju Szarotka', 3, 'Zakopane', cast('14:00' as time), cast('11:00' as time))", params);
    }

}
