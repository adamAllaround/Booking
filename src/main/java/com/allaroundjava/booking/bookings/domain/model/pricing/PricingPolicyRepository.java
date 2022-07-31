package com.allaroundjava.booking.bookings.domain.model.pricing;

import com.allaroundjava.booking.bookings.shared.Interval;

import java.util.UUID;

public interface PricingPolicyRepository {

    PricingPolicies findPoliciesFor(UUID roomId, Interval searchInterval);
}
