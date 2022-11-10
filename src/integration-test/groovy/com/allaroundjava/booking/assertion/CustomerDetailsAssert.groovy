package com.allaroundjava.booking.assertion

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


class CustomerDetailsAssert {
    private final NamedParameterJdbcTemplate jdbcTemplate

    CustomerDetailsAssert(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate
    }

    void hasCustomerDetailsFor(UUID reservationId) {
        Integer rowCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservationcustomers WHERE reservationId=:reservationId",["reservationId": reservationId], Integer.class)
        assert rowCount == 1
    }

    void emailIs(UUID reservationId, String email) {
        String persistedEmail = jdbcTemplate.queryForObject("SELECT email FROM reservationcustomers WHERE reservationId=:reservationId",["reservationId": reservationId], String.class)
        assert persistedEmail == email
    }

    void statusIsCustomerSpecified(UUID reservationId) {
        String status = jdbcTemplate.queryForObject("SELECT status FROM reservationdetails WHERE reservationId=:reservationId", ["reservationId": reservationId], String.class)
        assert status=="CUSTOMER_SPECIFIED"
    }

    void statusIsPaymentSpecified(UUID reservationId) {
        String status = jdbcTemplate.queryForObject("SELECT status FROM reservationdetails WHERE reservationId=:reservationId", ["reservationId": reservationId], String.class)
        assert status=="PAYMENT_SPECIFIED"
    }
}
