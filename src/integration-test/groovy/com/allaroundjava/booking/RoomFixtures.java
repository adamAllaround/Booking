package com.allaroundjava.booking;

import com.allaroundjava.booking.bookings.domain.model.Dates2020;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class RoomFixtures {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

        public void existsRoom(UUID roomId, UUID ownerId) {
        Map<String, Object> params = ImmutableMap.of("roomId", roomId,
                "ownerId", ownerId);

        namedParameterJdbcTemplate.update("insert into roommeta (id, ownerid, name, description, capacity, location, arrivalhour, departurehour) " +
                "VALUES (:roomId, :ownerId, 'Szarotka', 'Opis pokoju Szarotka', 3, 'Zakopane', cast('14:00' as time), cast('11:00' as time))", params);
    }

}
