package com.allaroundjava.booking.bookings.domain.ports;

import com.allaroundjava.booking.bookings.domain.model.RoomOccupation;
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent;

import java.util.UUID;

public interface RoomRepository {
    RoomOccupation findById(UUID id);

    void handle(OccupationEvent event);
}
