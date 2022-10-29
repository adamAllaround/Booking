package com.allaroundjava.booking.bookings.pricing;

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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
class JdbcPricingPolicyRepository implements PricingPolicyRepository {
    private static final String CURRENT_POLICIES = "select * from pricingpolicies where roomId in (:roomIds) and startTime <= cast(:intervalEnd as timestamp) and startTime >= cast(:intervalStart as timestamp)";
    private static final String UNION = "UNION";
    private static final String PREVIOUS_POLICY = "select * from (select * from pricingpolicies where roomId in (:roomIds) and startTime < cast(:intervalStart as timestamp) order by startTime fetch first 1 row only) previousPolicy";


    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Map<UUID, PricingPolicies> findPoliciesFor(Set<UUID> roomIds, Interval searchInterval) {
        ImmutableMap<String, Object> params = ImmutableMap.of(
                "roomIds", roomIds,
                "intervalStart", Timestamp.from(searchInterval.getStart()),
                "intervalEnd", Timestamp.from(searchInterval.getEnd()));

        List<PolicyEntity> policyEntities = jdbcTemplate.query(String.format("%s %s %s", CURRENT_POLICIES, UNION, PREVIOUS_POLICY),
                params,
                new BeanPropertyRowMapper<>(PolicyEntity.class));

        Map<UUID, List<PolicyEntity>> entitiesById = policyEntities.stream()
                .collect(Collectors.groupingBy(PolicyEntity::getRoomId));

        return entitiesById.entrySet()
                .stream()
                .map(entry -> toRoomsPolicies(entry, searchInterval))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<UUID, PricingPolicies> toRoomsPolicies(Map.Entry<UUID, List<PolicyEntity>> policyEntities, Interval searchInterval) {
        UUID roomId = policyEntities.getKey();
        List<PolicyEntity> policies = policyEntities.getValue();

        if (policies.isEmpty()) {
            throw new IllegalStateException("Could not find pricing policy entities");
        }

        if (policies.size() == 1) {
            return Map.entry(roomId, PricingPolicies.singleton(toPolicy(policies.get(0), searchInterval)));
        }

        Instant start = searchInterval.getStart();
        PricingPolicies pricingPolicies = PricingPolicies.empty();
        for (int i = 0; i < policies.size(); i++) {
            PolicyEntity currentPolicy = policies.get(i);

            if (i >= policies.size() - 1) {
                pricingPolicies = pricingPolicies.add(toPolicy(currentPolicy, new Interval(start, searchInterval.getEnd())));
            } else {
                PolicyEntity nextPolicy = policies.get(i + 1);
                Instant nextPolicyEnd = nextPolicy.getStartTime().toInstant();
                pricingPolicies = pricingPolicies.add(toPolicy(currentPolicy, new Interval(start, nextPolicyEnd)));
                start = nextPolicyEnd;
            }
        }
        return Map.entry(roomId, pricingPolicies);
    }

    private PricingPolicy toPolicy(PolicyEntity policyEntity, Interval interval) {
        if ("FLAT".equals(policyEntity.getPolicy())) {
            return PricingPolicy.flat(
                    new Money(new BigDecimal(policyEntity.getParameters())),
                    interval);
        }

        throw new IllegalStateException(String.format("Could not find matching policy for %s", policyEntity.getPolicy()));
    }
}

@Data
class PolicyEntity {
    UUID roomId;
    Timestamp startTime;
    String policy;
    String parameters;
}
