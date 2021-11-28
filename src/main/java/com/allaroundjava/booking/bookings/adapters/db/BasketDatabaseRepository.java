package com.allaroundjava.booking.bookings.adapters.db;

import com.allaroundjava.booking.bookings.domain.model.Basket;
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent;
import com.allaroundjava.booking.bookings.domain.ports.BasketRepository;
import com.google.common.collect.ImmutableMap;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
class BasketDatabaseRepository implements BasketRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void handle(OccupationEvent.BasketAddSuccess basketAddSuccess) {
        ImmutableMap<String, Object> params = ImmutableMap.of("id", basketAddSuccess.getBasketId(),
                "roomId", basketAddSuccess.getItemId(),
                "startTime", Timestamp.from(basketAddSuccess.getInterval().getStart()),
                "endTime", Timestamp.from(basketAddSuccess.getInterval().getEnd()));
        jdbcTemplate.update("insert into Baskets (id, roomId, startTime, endTime) values (:id,:roomId, :startTime, :endTime);", params);
    }

    @Override
    public Optional<Basket> getSingle(UUID basketId) {
        ImmutableMap<String, UUID> params = ImmutableMap.of("id", basketId);
        return Try.of(() -> queryForSingle(params))
                .map(BasketDatabaseEntity::toModel)
                .map(Optional::of)
                .getOrElseGet((throwable) -> Optional.empty());
    }

    private BasketDatabaseEntity queryForSingle(ImmutableMap<String, UUID> params) {
        return jdbcTemplate.queryForObject("SELECT b.* FROM Baskets b where b.id=:id",
                params,
                new BasketDatabaseEntity.RowMapper());
    }
}
