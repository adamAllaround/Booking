package com.allaroundjava.booking

import com.google.common.collect.ImmutableMap
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

import java.sql.Date
import java.time.LocalDate

class PreBookingFixtures {
    private NamedParameterJdbcTemplate jdbcTemplate

    PreBookingFixtures(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate
    }

    void preBook(UUID reservationId, UUID roomId, LocalDate dateFrom, LocalDate dateTo) {
        def params = ImmutableMap.<String, Object> builder()
                .put("reservationId", reservationId)
                .put("roomId", roomId)
                .put("dateFrom", Date.valueOf(dateFrom))
                .put("dateTo", Date.valueOf(dateTo))
                .build()

        jdbcTemplate.update("INSERT INTO prebookings (reservationId, roomId, dateFrom, dateTo) values (:reservationId, :roomId, :dateFrom, :dateTo)", params)
    }
}
