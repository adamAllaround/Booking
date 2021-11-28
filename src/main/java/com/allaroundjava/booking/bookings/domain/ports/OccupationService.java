package com.allaroundjava.booking.bookings.domain.ports;

import com.allaroundjava.booking.bookings.domain.command.AddAvailabilityCommand;
import com.allaroundjava.booking.bookings.domain.model.*;
import com.allaroundjava.booking.common.events.EventPublisher;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
public class OccupationService {
    private final OccupationRepository occupationRepository;
    private final EventPublisher eventPublisher;
    private final BasketRepository basketRepository;

    @Transactional
    public Either<OccupationEvent.AddAvailabilityFailure, OccupationEvent.AddAvailabilitySuccess> addAvailabilities(AddAvailabilityCommand addAvailabilityCommand) {
        RoomOccupation room = occupationRepository.find(addAvailabilityCommand.getRoomId(), addAvailabilityCommand.getInterval());
        return room.addAvailability(addAvailabilityCommand.getInterval())
                .peek(occupationRepository::handle);
    }

    @Transactional
    public Either<OccupationEvent.BasketAddFailure, OccupationEvent.BasketAddSuccess> addBasket(Basket basket) {
        RoomOccupation room = occupationRepository.find(basket.getRoomId(), basket.getInterval());

        return room.canBook(basket)
                .peek(basketRepository::handle);
    }
}
