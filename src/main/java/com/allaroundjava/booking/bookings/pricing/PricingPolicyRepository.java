package com.allaroundjava.booking.bookings.pricing;

import com.allaroundjava.booking.bookings.shared.Interval;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

interface PricingPolicyRepository {

    Map<UUID, PricingPolicies> findPoliciesFor(Set<UUID> roomId, Interval searchInterval);
}
