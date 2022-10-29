package com.allaroundjava.booking.bookings.details;

import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ReservationDetails {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void initialize(UUID reservationId, UUID roomId, LocalDate dateFrom, LocalDate dateTo, int guests) {
        Map<String, Object> params = ImmutableMap.<String, Object>builder()
                .put("reservationId", reservationId)
                .put("roomId", roomId)
                .put("dateFrom", java.sql.Date.valueOf(dateFrom))
                .put("dateTo", java.sql.Date.valueOf(dateTo))
                .put("guests", guests)
                .put("status", "INITIALIZED")
                .build();

        jdbcTemplate.update("INSERT INTO reservationdetails (reservationId, roomId, dateFrom, dateTo, guests, status) values (:reservationId, :roomId, :dateFrom, :dateTo, :guests, :status)", params);
    }
}
//this is only a crud module