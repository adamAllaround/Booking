package com.allaroundjava.booking.domain.pricing;

import com.allaroundjava.booking.common.Interval;
import com.allaroundjava.booking.common.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QueryForPrice {
    private final PricingPolicyRepository policyRepository;
    public RoomPrices priceFor(Set<UUID> roomIds, Interval searchInterval) {
        Map<UUID, PricingPolicies> policiesInRoom = policyRepository.findPoliciesFor(roomIds, searchInterval);
        Map<UUID, Money> roomTotals = policiesInRoom.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getTotal()));
        return new RoomPrices(roomTotals);
    }

    public Money priceFor(UUID roomId, Interval searchInterval) {
        return priceFor(Collections.singleton(roomId), searchInterval)
                .of(roomId)
                .orElseThrow(() -> new IllegalStateException("Could not find price for asked roomId"));
    }
}
