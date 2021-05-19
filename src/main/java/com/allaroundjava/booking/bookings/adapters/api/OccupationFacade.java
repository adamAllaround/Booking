package com.allaroundjava.booking.bookings.adapters.api;

import com.allaroundjava.booking.bookings.domain.ports.OccupationService;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class OccupationFacade {
    private final OccupationService occupationService;
    public Optional<AvailabilityResponse> save(UUID itemId, AvailabilityRequest request) {
        return occupationService.addAvailability(itemId, request.toDomainWithItemId(itemId))
                .map(success -> AvailabilityResponse.from(success.getAvailability()))
                .map(Optional::of)
                .getOrElse(Optional::empty);
    }
}
