package com.allaroundjava.booking;

import com.allaroundjava.booking.bookings.domain.model.Availability;
import com.allaroundjava.booking.bookings.domain.model.Interval;
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent;
import com.allaroundjava.booking.bookings.domain.ports.OccupationRepository;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

@RequiredArgsConstructor
public class AvailabilityFixtures {
    private final OccupationRepository occupationRepository;

    public void existsAvailability(UUID roomId, Instant from, Instant to) {
        Availability availability = new Availability(UUID.randomUUID(), roomId, new Interval(from, to));
        occupationRepository.handle(new OccupationEvent.AddAvailabilitySuccess(roomId, Collections.singletonList(availability)));
    }
}
