package com.allaroundjava.booking.owners;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
class OwnersRepository {
    private final JdbcTemplate jdbcTemplate;

    Collection<Owner> getAll() {
        return jdbcTemplate.queryForList("SELECT o.* FROM Owners o", OwnerDatabaseEntity.class)
                .stream()
                .map(OwnerDatabaseEntity::toDomainModel)
                .collect(Collectors.toList());
    }

    Optional<Owner> getSingle(UUID id) {
        OwnerDatabaseEntity ownerDatabaseEntity = jdbcTemplate.queryForObject("SELECT o.* FROM Owners o where id=?",
                new BeanPropertyRowMapper<>(OwnerDatabaseEntity.class), id);

        return Optional.ofNullable(ownerDatabaseEntity)
                .map(OwnerDatabaseEntity::toDomainModel);
    }

    Owner save(Owner owner) {
        jdbcTemplate.update("INSERT INTO Owners (id, name, email) values (?,?,?)",
                owner.getId(), owner.getName(), owner.getEmail());
        return owner;
    }

    @Data
    @NoArgsConstructor
    static class OwnerDatabaseEntity {
        private UUID id;
        private String name;
        private String email;

        Owner toDomainModel() {
            Owner owner = new Owner();
            owner.setId(id);
            owner.setName(name);
            owner.setEmail(email);
            return owner;
        }
    }

}
