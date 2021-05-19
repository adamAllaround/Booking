package com.allaroundjava.booking.bookings.adapters.db;

import com.allaroundjava.booking.bookings.domain.model.Availability;
import com.allaroundjava.booking.bookings.domain.ports.AvailabilitiesRepository;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
class AvailabilitiesDatabaseRepository implements AvailabilitiesRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    @Override
    public List<Availability> getAllByItemId(UUID itemId) {
        ImmutableMap<String, UUID> params = ImmutableMap.of("itemId", itemId);

        return jdbcTemplate.query("select a.* from Availabilities a where a.itemId=:id",
                params,
                new BeanPropertyRowMapper<>(AvailabilityDatabaseEntity.class))
                .stream()
                .map(AvailabilityDatabaseEntity::toModel)
                .collect(Collectors.toList());
    }
}
