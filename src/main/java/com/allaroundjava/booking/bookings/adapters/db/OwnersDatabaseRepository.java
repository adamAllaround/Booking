package com.allaroundjava.booking.bookings.adapters.db;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
public class OwnersDatabaseRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OwnerDatabaseEntity save(OwnerDatabaseEntity owner) {
        ImmutableMap<String, Object> params = ImmutableMap.of("id", owner.getId(),
                "name", owner.getName(),
                "email", owner.getEmail(),
                "created", Timestamp.from(owner.getCreated()));
        jdbcTemplate.update("INSERT INTO Owners (id, name, email, created) values (:id, :name, :email, :created)", params);
        return owner;
    }

    @Value
    public static class OwnerDatabaseEntity {
        UUID id;
        String name;
        String email;
        Instant created;

        public static OwnerDatabaseEntity now(String name, String email) {
            return new OwnerDatabaseEntity(UUID.randomUUID(), name, email, Instant.now());
        }
    }
}


