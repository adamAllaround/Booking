package com.allaroundjava.booking.bookings.adapters.db;

import com.allaroundjava.booking.bookings.domain.model.Booking;
import com.allaroundjava.booking.bookings.domain.ports.BookingsRepository;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
class BookingsDatabaseRepository implements BookingsRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Booking> getAllByItemId(UUID itemId) {
        ImmutableMap<String, UUID> params = ImmutableMap.of("itemId", itemId);
        return jdbcTemplate.query("SELECT b.* FROM Bookings b where b.itemId=:itemId",
                params,
                new BeanPropertyRowMapper<>(BookingDatabaseEntity.class))
                .stream()
                .map(BookingDatabaseEntity::toModel)
                .collect(Collectors.toList());
    }
}
