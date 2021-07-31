package com.allaroundjava.booking.bookings.adapters.db;

import com.allaroundjava.booking.bookings.domain.model.Booking;
import com.allaroundjava.booking.bookings.domain.ports.BookingsRepository;
import com.google.common.collect.ImmutableMap;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
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
                new BookingDatabaseEntity.RowMapper())
                .stream()
                .map(BookingDatabaseEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Booking> getSingle(UUID bookingId) {
        ImmutableMap<String, UUID> params = ImmutableMap.of("id", bookingId);
        return Try.of(() -> queryForSingle(params))
                .map(BookingDatabaseEntity::toModel)
                .map(Optional::of)
                .getOrElseGet((throwable) -> Optional.empty());
    }

    private BookingDatabaseEntity queryForSingle(ImmutableMap<String, UUID> params) {
        return jdbcTemplate.queryForObject("SELECT * FROM Bookings where id=:id",
                params,
                new BookingDatabaseEntity.RowMapper());
    }
}
