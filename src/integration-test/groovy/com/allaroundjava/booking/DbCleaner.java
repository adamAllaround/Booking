package com.allaroundjava.booking;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

@AllArgsConstructor
public class DbCleaner {
    private final JdbcTemplate jdbcTemplate;

    public void cleanAvailabilities() {
        jdbcTemplate.update("DELETE FROM Availabilities");
    }

    public void cleanItems() {
        jdbcTemplate.update("DELETE FROM OccupationItems");
    }
}
