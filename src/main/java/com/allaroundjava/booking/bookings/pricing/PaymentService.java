package com.allaroundjava.booking.bookings.pricing;

import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void setPayment(UUID reservationId, PaymentType type) {
        Map<String, Object> params = ImmutableMap.<String, Object>builder()
                .put("reservationId", reservationId)
                .put("type", type.name())
                .build();
        jdbcTemplate.update("INSERT INTO payments (reservationId, type) values (:reservationId, :type)", params);
    }
}
//just crud for now