package com.allaroundjava.booking.bookings.adapters.api;

import com.allaroundjava.booking.bookings.domain.ports.OccupationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
class OccupationFacade {
    private final OccupationService occupationService;
    public Optional<AvailabilitiesResponse> save(UUID itemId, AvailabilityRequest request) {
        return occupationService.addAvailabilities(itemId, request.toDomainWithItemId(itemId))
                .map(success -> AvailabilitiesResponse.from(success.getAvailabilityList()))
                .map(Optional::of)
                .getOrElse(Optional::empty);
    }
}
