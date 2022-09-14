package com.allaroundjava.booking.bookings.domain.model.pricing;

import com.allaroundjava.booking.bookings.shared.Interval;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface PricingPolicyRepository {

    Map<UUID, PricingPolicies> findPoliciesFor(Set<UUID> roomId, Interval searchInterval);
}
