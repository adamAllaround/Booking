package com.allaroundjava.booking.bookings.adapters.db;

import com.allaroundjava.booking.bookings.domain.ports.ReservationPricesRepository;
import com.allaroundjava.booking.bookings.shared.Money;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
class JdbcReservationPricesRepository implements ReservationPricesRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void setReservationPrice(UUID reservationId, Money money) {
        Map<String, Object> params = ImmutableMap.of(
                "reservationId", reservationId,
                "amount", money.getDenomination()
        );

        jdbcTemplate.update("INSERT INTO reservationprices (reservationId, amount) values (:reservationId, :amount)", params);
    }
}
