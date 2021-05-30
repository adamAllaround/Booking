package com.allaroundjava.booking.bookings.adapters.api;

import com.allaroundjava.booking.bookings.domain.model.Availabilities;
import com.allaroundjava.booking.bookings.domain.model.Availability;
import com.allaroundjava.booking.bookings.domain.ports.AvailabilitiesService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
class AvailabilitiesFacade {
    private final AvailabilitiesService availabilitiesService;
    AvailabilitiesResponse getAllByItemId(UUID itemId) {
        List<Availability> availabilities = availabilitiesService.getAllByItemId(itemId);
        return AvailabilitiesResponse.from(availabilities);
    }}
