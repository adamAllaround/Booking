package com.allaroundjava.booking.bookings.domain.ports;

import com.allaroundjava.booking.bookings.domain.model.Availability;
import com.allaroundjava.booking.bookings.domain.model.Occupation;
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class OccupationService {
    private final OccupationRepository occupationRepository;

    public Either<OccupationEvent.AddAvailabilityFailure, OccupationEvent.AddAvailabilitySuccess> addAvailability(UUID itemId, Availability availability) {
        Occupation occupation = occupationRepository.findById(itemId);
        return occupation.addAvailability(itemId, availability.getInterval());
    }
}
