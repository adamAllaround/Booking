package com.allaroundjava.booking.bookings.adapters.db;

import com.allaroundjava.booking.bookings.domain.model.pricing.FlatPrice;
import com.allaroundjava.booking.bookings.domain.model.pricing.PricingPolicies;
import com.allaroundjava.booking.bookings.domain.model.pricing.PricingPolicy;
import com.allaroundjava.booking.bookings.domain.model.pricing.PricingPolicyRepository;
import com.allaroundjava.booking.bookings.shared.Interval;
import com.allaroundjava.booking.bookings.shared.Money;
import com.google.common.collect.ImmutableMap;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
class JdbcPricingPolicyRepository implements PricingPolicyRepository {
    private static final String SELECT_POLICIES = "select * from pricingpolicies where startTime = " +
                    "(select max(startTime) from pricingpolicies where startTime <=:start) " +
                    "union select * from pricingpolicies where starttime = " +
                    "(select max(starttime) from pricingpolicies where starttime < :end)" +
                    "order by starttime asc";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public PricingPolicies findPoliciesFor(UUID roomId, Interval searchInterval) {
        ImmutableMap<String, Object> params = ImmutableMap.of(
                "start", Timestamp.from(searchInterval.getStart()),
                "end", Timestamp.from(searchInterval.getEnd()));

        List<PolicyEntity> policyEntities = jdbcTemplate.query(SELECT_POLICIES,
                params,
                new BeanPropertyRowMapper<PolicyEntity>());

        return toPricingPolicies(policyEntities, searchInterval);
    }

    private PricingPolicies toPricingPolicies(List<PolicyEntity> policyEntities, Interval searchInterval) {
        if(policyEntities.isEmpty())
        {
            throw new IllegalStateException("Could not find pricing policy entities");
        }

        if (policyEntities.size() == 1) {
            return PricingPolicies.singleton(toPolicy(policyEntities.get(0), searchInterval));
        }


        Instant start = searchInterval.getStart();
        PricingPolicies pricingPolicies = PricingPolicies.empty();
        for (int i = 1; i < policyEntities.size(); i++) {
            PolicyEntity currentPolicy = policyEntities.get(i - 1);

            if(i >= policyEntities.size() -1) {
                pricingPolicies = pricingPolicies.add(toPolicy(currentPolicy, new Interval(start, searchInterval.getEnd())));
            } else {
                PolicyEntity nextPolicy = policyEntities.get(i);
                Instant nextPolicyEnd = nextPolicy.getStartTime().toInstant();
                pricingPolicies = pricingPolicies.add(toPolicy(currentPolicy, new Interval(start, nextPolicyEnd)));
                start = nextPolicyEnd;
            }

        }
        return pricingPolicies;
    }

    private PricingPolicy toPolicy(PolicyEntity policyEntity, Interval interval) {
        if ("FLAT".equals(policyEntity.getPolicy())) {
            return new FlatPrice(
                    new Money(new BigDecimal(policyEntity.getParameters()), Currency.getInstance("PLN")),
                    interval);
        }

        throw new IllegalStateException(String.format("Could not find matching policy for %s", policyEntity.getPolicy()));
    }

    @Data
    class PolicyEntity {
        UUID roomId;
        Timestamp startTime;
        String policy;
        String parameters;
    }
}
