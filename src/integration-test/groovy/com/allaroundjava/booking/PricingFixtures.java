package com.allaroundjava.booking;

import com.allaroundjava.booking.common.Money;
import com.google.common.collect.ImmutableMap;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Map;
import java.util.UUID;

public class PricingFixtures {
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PricingFixtures(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {

        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void hasFlatPrice(UUID roomId, Money price) {
        Map<String, Object> params = ImmutableMap.of("roomId", roomId,
                "price", price.toCurrencyLessString());

        namedParameterJdbcTemplate.update("insert into pricingpolicies (roomId, starttime, policy, parameters) values (:roomId, cast('2020-02-02' as timestamp), 'FLAT', :price)", params);
    }
}
