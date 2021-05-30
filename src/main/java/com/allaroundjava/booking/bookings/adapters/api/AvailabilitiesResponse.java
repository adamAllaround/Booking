package com.allaroundjava.booking.bookings.adapters.api;

import com.allaroundjava.booking.bookings.domain.model.Availability;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
class AvailabilitiesResponse {
    Collection<AvailabilityResponse> availabilities;

    static AvailabilitiesResponse from(List<Availability> availabilities) {
        List<AvailabilityResponse> result = availabilities.stream()
                .map(AvailabilityResponse::from)
                .collect(Collectors.toList());
        return new AvailabilitiesResponse(result);
    }
}
