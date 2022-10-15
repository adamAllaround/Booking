package com.allaroundjava.booking;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

@AllArgsConstructor
public class DbCleaner {
    private final JdbcTemplate jdbcTemplate;

    public void cleanPrices() {
        jdbcTemplate.update("DELETE FROM pricingpolicies");
    }

    public void cleanAvailabilities() {
        jdbcTemplate.update("DELETE FROM Availabilities");
    }

    public void cleanRooms() {
        jdbcTemplate.update("DELETE FROM roommeta");
    }

    public void cleanBookings() {
        jdbcTemplate.update("DELETE FROM reservations");
    }
}
