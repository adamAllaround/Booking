package com.allaroundjava.booking.bookings.adapters.db;

import com.allaroundjava.booking.bookings.domain.model.Availability;
import com.allaroundjava.booking.bookings.domain.ports.AvailabilitiesRepository;
import com.google.common.collect.ImmutableMap;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
class AvailabilitiesDatabaseRepository implements AvailabilitiesRepository { //TODO - rethink - this is a read model
    private final NamedParameterJdbcTemplate jdbcTemplate;

//    @Override
//    public List<Availability> getAllByItemId(UUID itemId) {
//        ImmutableMap<String, UUID> params = ImmutableMap.of("itemId", itemId);
//
//        return jdbcTemplate.query("select a.* from Availabilities a where a.itemId=:itemId",
//                params,
//                new AvailabilityDatabaseEntity.RowMapper())
//                .stream()
//                .map(AvailabilityDatabaseEntity::toModel)
//                .collect(Collectors.toList());
//    }

    @Override
    public Optional<Availability> getSingle(UUID uuid) {
        ImmutableMap<String, UUID> params = ImmutableMap.of("id", uuid);
        return Try.of(() -> queryForSingle(params))
                .map(AvailabilityDatabaseEntity::toModel)
                .map(Optional::of)
                .getOrElseGet((throwable) -> Optional.empty());
    }

    private AvailabilityDatabaseEntity queryForSingle(ImmutableMap<String, UUID> params) {
        return jdbcTemplate.queryForObject("SELECT a.* FROM Availabilities a where a.id=:id",
                params,
                new AvailabilityDatabaseEntity.RowMapper());
    }

    @Override
    public List<Availability> getAllByIds(Collection<UUID> ids) {
        ImmutableMap<String, Object> params = ImmutableMap.of("availabilityIds", ids);

        return jdbcTemplate.query("SELECT a.* FROM Availabilities a where a.id in (:availabilityIds)",
                params,
                new BeanPropertyRowMapper<>(AvailabilityDatabaseEntity.class))
                .stream()
                .map(AvailabilityDatabaseEntity::toModel)
                .collect(Collectors.toList());
    }
}
