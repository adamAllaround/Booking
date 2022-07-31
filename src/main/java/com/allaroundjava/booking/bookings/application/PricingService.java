package com.allaroundjava.booking.bookings.application;

import com.allaroundjava.booking.bookings.domain.model.pricing.PricingPolicies;
import com.allaroundjava.booking.bookings.domain.model.pricing.PricingPolicyRepository;
import com.allaroundjava.booking.bookings.shared.Interval;
import com.allaroundjava.booking.bookings.shared.Money;

import java.util.Collection;
import java.util.UUID;

public class PricingService {
    PricingPolicyRepository policyRepository;
    RoomPrices priceFor(Collection<UUID> roomIds, Interval searchInterval) {
        return null;
    }

    Money priceFor(UUID roomId, Interval searchInterval) {
        PricingPolicies policies = policyRepository.findPoliciesFor(roomId, searchInterval);
        return policies.getTotal();
    }
}
