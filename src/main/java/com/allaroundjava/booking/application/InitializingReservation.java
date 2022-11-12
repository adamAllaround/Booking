package com.allaroundjava.booking.application;

import com.allaroundjava.booking.domain.availability.RoomAvailability;
import com.allaroundjava.booking.domain.details.ReservationDetails;
import com.allaroundjava.booking.domain.pricing.PricingService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class InitializingReservation {
    private final RoomAvailability roomAvailability;
    private final PricingService pricingService;
    private final ReservationDetails reservationDetails;
    public Optional<UUID> initialize(InitializeReservationCommand command) {
        if (!roomAvailability.isAvailable(command.getRoomId(), command.getDateFrom(), command.getDateTo())) {
            return Optional.empty();
        }

        UUID reservationId = UUID.randomUUID();
        roomAvailability.preBook(reservationId, command.getRoomId(), command.getDateFrom(), command.getDateTo());
        pricingService.setPrice(reservationId, command.getRoomId(), command.getDateFrom(), command.getDateTo(), command.getGuests());
        reservationDetails.initialize(reservationId, command.getRoomId(), command.getDateFrom(), command.getDateTo(), command.getGuests());
        return Optional.of(reservationId);
    }
}
