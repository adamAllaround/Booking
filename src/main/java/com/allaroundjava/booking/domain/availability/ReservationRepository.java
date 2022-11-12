package com.allaroundjava.booking.domain.availability;

import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
@RequiredArgsConstructor
class ReservationRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    void reserve(Reservation reservation) {
        ImmutableMap<String, Object> params = ImmutableMap.<String, Object>builder()
                .put("reservationId", reservation.getId())
                .put("roomId", reservation.getRoomId())
                .put("dateFrom", Date.valueOf(reservation.getFrom()))
                .put("dateTo", Date.valueOf(reservation.getTo()))
                .build();

        jdbcTemplate.update("INSERT INTO reservations (reservationId, roomId, dateFrom, dateTo) values (:reservationId, :roomId, :dateFrom, :dateTo)", params);
    }
}
