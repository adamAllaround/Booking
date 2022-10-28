package com.allaroundjava.booking.bookings.application;

import java.util.Optional;
import java.util.UUID;

public class InitializingReservation {
    private final RoomAvailability roomAvailability;
    private final PricingService roomPricing;
    private final ReservationDetails reservationDetails;
    public Optional<UUID> initialize(InitializeReservationCommand command) {
        if (!roomAvailability.isAvailable(command.getRoomId(), command.getDateFrom(), command.getDateTo())) {
            return Optional.empty();
        }

        UUID reservationId = UUID.randomUUID();
        roomPricing.setPrice(reservationId, command.getRoomId(), command.getDateFrom(), command.getDateTo(), command.getGuests());
        reservationDetails.preBook(reservationId, command.getRoomId(), command.getDateFrom(), command.getDateTo(), command.getGuests());
        return Optional.of(reservationId);
    }
}
