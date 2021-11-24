package com.allaroundjava.booking.bookings.domain.ports;

import com.allaroundjava.booking.bookings.domain.model.*;
import com.allaroundjava.booking.common.events.EventPublisher;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class OccupationService {
    private final RoomRepository roomRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public Either<OccupationEvent.AddAvailabilityFailure, OccupationEvent.AddAvailabilitySuccess> addAvailabilities(UUID itemId, Availability availability) {
        RoomOccupation room = roomRepository.findById(itemId);
        return room.addAvailability(availability.getInterval())
                .peek(roomRepository::handle);
    }

    @Transactional
    public Either<OccupationEvent.BookingFailure, OccupationEvent.BookingSuccess> addBooking(Booking booking) {
        RoomOccupation room = roomRepository.findById(booking.getItemId());
        return room.addBooking(booking)
                .peek(roomRepository::handle)
                .peek(eventPublisher::publish);
    }

    @Transactional
    public Either<OccupationEvent.BasketAddFailure, OccupationEvent.BasketAddSuccess> addBasket(UUID itemId, OffsetDateTime dateStart, OffsetDateTime dateEnd) {
        RoomOccupation room = roomRepository.findById(itemId);
        return room.addBasket(dateStart, dateEnd)
                .peek(roomRepository::handle);
    }
}
