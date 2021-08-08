package com.allaroundjava.booking.notifications.owners;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class OwnersRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    void save(Owner owner) {
        ImmutableMap<String, Object> params = ImmutableMap.of(
                "id", owner.getId(),
                "email", owner.getEmail());

        jdbcTemplate.update("INSERT INTO NotificationsOwners (id, email) values (:id, :email)",
                params);
    }

    public Optional<Owner> findById(UUID id) {
        ImmutableMap<String, Object> params = ImmutableMap.of("id", id);
        try {
            OwnerDatabaseEntity ownerDatabaseEntity = jdbcTemplate.queryForObject("SELECT * FROM NotificationsOwners o where id=:id",
                    params, new BeanPropertyRowMapper<>(OwnerDatabaseEntity.class));
            return Optional.ofNullable(ownerDatabaseEntity.toDomainModel());
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Data
    @NoArgsConstructor
    static class OwnerDatabaseEntity {
        private UUID id;
        private String email;

        Owner toDomainModel() {
            return new Owner(id, email);
        }
    }
}
