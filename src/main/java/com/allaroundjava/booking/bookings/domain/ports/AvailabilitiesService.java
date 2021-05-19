package com.allaroundjava.booking.bookings.domain.ports;

import com.allaroundjava.booking.bookings.domain.model.Availabilities;
import com.allaroundjava.booking.bookings.domain.model.Availability;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class AvailabilitiesService {
    private final AvailabilitiesRepository repository;
    public Availabilities getAllByItemId(UUID itemId) {
        List<Availability> availabilities = repository.getAllByItemId(itemId);
        return new Availabilities(availabilities);
    }
}
