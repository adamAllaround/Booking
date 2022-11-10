package com.allaroundjava.booking.assertion


import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class PaymentDetailsAssert {
    private final NamedParameterJdbcTemplate jdbcTemplate

    PaymentDetailsAssert(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate
    }

    void paymentTypeIs(UUID reservationId, String expectedPaymentType) {
        String status = jdbcTemplate.queryForObject("SELECT type FROM payments WHERE reservationId=:reservationId", ["reservationId": reservationId], String.class)
        assert status==expectedPaymentType
    }
}
