package com.allaroundjava.booking.assertion

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

import java.time.LocalDate

class ReservationAssert {
    private final NamedParameterJdbcTemplate jdbcTemplate

    ReservationAssert(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate
    }

    void existsReservationFor(UUID reservationId, UUID roomId) {
        assert countReservationsFor(reservationId, roomId) > 0
    }

    void reservedBetween(UUID reservationId, LocalDate dateFrom, LocalDate dateTo) {
        Integer rowCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservations WHERE reservationId=:reservationId and dateFrom=:dateFrom and dateTo=:dateTo",
                ["reservationId": reservationId,
                 "dateFrom": dateFrom,
                "dateTo": dateTo], Integer.class)
        assert rowCount > 0
    }

    void noReservationFor(UUID reservationId, UUID roomId) {
        assert countReservationsFor(reservationId, roomId) ==0
    }

    private int countReservationsFor(UUID reservationId, UUID roomId) {
        Integer rowCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservations WHERE reservationId=:reservationId and roomId=:roomId",
                ["reservationId": reservationId,
                 "roomId"       : roomId], Integer.class)
        return rowCount
    }
}
