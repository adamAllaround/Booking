package com.allaroundjava.booking.bookings.domain.pricing;

import com.allaroundjava.booking.bookings.shared.Money;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class PricingPolicies {
    private final Set<PricingPolicy> policies;

    public static PricingPolicies singleton(PricingPolicy Policy) {
        return new PricingPolicies(Collections.singleton(Policy));
    }

    public static PricingPolicies empty() {
        return new PricingPolicies(Collections.emptySet());
    }

    public Money getTotal() {
        return policies.stream()
                .map(PricingPolicy::calculatePrice)
                .reduce(Money.ZERO, Money::add);
    }


    public PricingPolicies add(PricingPolicy toPolicy) {
        Set<PricingPolicy> newPolicies = new HashSet<>(policies);
        newPolicies.add(toPolicy);
        return new PricingPolicies(newPolicies);
    }
}
