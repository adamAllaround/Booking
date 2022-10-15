package com.allaroundjava.booking.bookings.application;

import com.allaroundjava.booking.bookings.domain.ports.PricingPolicyRepository;
import com.allaroundjava.booking.bookings.domain.pricing.PricingPolicies;
import com.allaroundjava.booking.bookings.shared.Interval;
import com.allaroundjava.booking.bookings.shared.Money;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PricingService {
    private final PricingPolicyRepository policyRepository;
    public RoomPrices priceFor(Set<UUID> roomIds, Interval searchInterval) {
        Map<UUID, PricingPolicies> policiesInRoom = policyRepository.findPoliciesFor(roomIds, searchInterval);
        Map<UUID, Money> roomTotals = policiesInRoom.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getTotal()));
        return new RoomPrices(roomTotals);
    }

    Money priceFor(UUID roomId, Interval searchInterval) {
        return priceFor(Collections.singleton(roomId), searchInterval)
                .of(roomId)
                .orElseThrow(() -> new IllegalStateException("Could not find price for asked roomId"));
    }
}
