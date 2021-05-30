package com.allaroundjava.booking.bookings.domain.ports;

import com.allaroundjava.booking.bookings.domain.model.Availability;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class AvailabilitiesService {
    private final AvailabilitiesRepository repository;
    public List<Availability> getAllByItemId(UUID itemId) {
        return repository.getAllByItemId(itemId);

    }
}
