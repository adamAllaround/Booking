package com.allaroundjava.booking.bookings.adapters.api;

import com.allaroundjava.booking.bookings.domain.model.Availabilities;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
class AvailabilitiesResponse {
    Collection<AvailabilityResponse> availabilities;

    static AvailabilitiesResponse from(Availabilities availabilities) {
        List<AvailabilityResponse> result = availabilities.map(AvailabilityResponse::from).collect(Collectors.toList());
        return new AvailabilitiesResponse(result);
    }
}
