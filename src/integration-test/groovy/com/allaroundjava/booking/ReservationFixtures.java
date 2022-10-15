package com.allaroundjava.booking;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class ReservationFixtures {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void roomIsBooked(UUID roomId, LocalDate reservationStart, LocalDate reservationEnd) {
        Map<String, Object> params = ImmutableMap.of("roomId", roomId,
                "reservationId", UUID.randomUUID(),
                "reservationStart", java.sql.Date.valueOf(reservationStart),
                "reservationEnd", java.sql.Date.valueOf(reservationEnd));

        jdbcTemplate.update("insert into reservations (reservationId, roomId, dateFrom, dateTo) values (:reservationId, :roomId, :reservationStart, :reservationEnd)",
                params);
    }
}