package com.allaroundjava.booking.notifications.owners;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.Timestamp;
import java.time.Instant;

@AllArgsConstructor
class OwnersRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    void save(Owner owner) {
        ImmutableMap<String, Object> params = ImmutableMap.of(
                "id", owner.getId(),
                "created", Timestamp.from(Instant.now()),
                "email", owner.getEmail());

        jdbcTemplate.update("INSERT INTO NotificationOwners (id, created, email) values (:id, :created, :email)",
                params);
    }
}
