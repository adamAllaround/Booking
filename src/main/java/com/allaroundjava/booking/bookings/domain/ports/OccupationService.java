package com.allaroundjava.booking.bookings.domain.ports;

import com.allaroundjava.booking.bookings.domain.model.Availability;
import com.allaroundjava.booking.bookings.domain.model.Booking;
import com.allaroundjava.booking.bookings.domain.model.Occupation;
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent;
import com.allaroundjava.booking.common.events.EventPublisher;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@AllArgsConstructor
public class OccupationService {
    private final OccupationRepository occupationRepository;
    private final EventPublisher eventPublisher;

    public Either<OccupationEvent.AddAvailabilityFailure, OccupationEvent.AddAvailabilitySuccess> addAvailabilities(UUID itemId, Availability availability) {
        Occupation occupation = occupationRepository.findById(itemId);
        return occupation.addAvailability(itemId, availability.getInterval())
                .peek(occupationRepository::handle);
    }

    @Transactional
    public Either<OccupationEvent.BookingFailure, OccupationEvent.BookingSuccess> addBooking(Booking booking) {
        Occupation occupation = occupationRepository.findById(booking.getItemId());
        return occupation.addBooking(booking)
                .peek(occupationRepository::handle)
                .peek(eventPublisher::publish);
    }
}
