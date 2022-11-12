package com.allaroundjava.booking.domain.pricing;

import com.allaroundjava.booking.common.Interval;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

interface PricingPolicyRepository {

    Map<UUID, PricingPolicies> findPoliciesFor(Set<UUID> roomId, Interval searchInterval);
}
