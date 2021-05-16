package com.allaroundjava.booking.bookings.domain.ports;

import com.allaroundjava.booking.bookings.domain.model.Occupation;
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent;

import java.util.UUID;

public interface OccupationRepository {
    Occupation findById(UUID id);

    void handle(OccupationEvent event);
}
