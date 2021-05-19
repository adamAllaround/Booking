package com.allaroundjava.booking.bookings.adapters.api;

import com.allaroundjava.booking.bookings.domain.model.Availabilities;
import lombok.Data;

import java.util.Collection;

@Data
class AvailabilitiesResponse {
    Collection<AvailabilityResponse> availabilities;

    static AvailabilitiesResponse from(Availabilities availabilities) {
        return null;
    }
}
