package com.allaroundjava.booking.items;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
class OwnersRepository {
    private final JdbcTemplate jdbcTemplate;

    public Owner save(Owner owner) {
        jdbcTemplate.update("INSERT INTO ItemsOwners (id, created) values (?, ? )", owner.getId(), Timestamp.from(owner.getCreated()));
        return owner;
    }

    public Optional<Owner> getSingle(UUID uuid) {
        OwnerDatabaseEntity ownerDatabaseEntity = jdbcTemplate.queryForObject("SELECT o.* FROM ItemsOwners o where id=?",
                new BeanPropertyRowMapper<>(OwnerDatabaseEntity.class), uuid);
        return Optional.ofNullable(ownerDatabaseEntity)
                .map(OwnerDatabaseEntity::toModel);
    }

    @Data
    static class OwnerDatabaseEntity {
        UUID id;
        Instant created;

        Owner toModel() {
            return new Owner(id, created);
        }
    }
}
