package com.allaroundjava.booking.bookings.domain.ports;

import com.allaroundjava.booking.bookings.domain.model.Interval;
import com.allaroundjava.booking.bookings.domain.model.RoomOccupation;
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent;

import java.util.UUID;

public interface OccupationRepository {

    void handle(OccupationEvent event);

    RoomOccupation find(UUID roomId, Interval interval);
}
