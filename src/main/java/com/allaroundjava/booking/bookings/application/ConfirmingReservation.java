package com.allaroundjava.booking.bookings.application;

import com.allaroundjava.booking.bookings.availability.RoomAvailability;
import com.allaroundjava.booking.bookings.details.ReservationDetails;
import com.allaroundjava.booking.bookings.shared.Failure;
import com.allaroundjava.booking.bookings.shared.Success;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class ConfirmingReservation {
    private final RoomAvailability roomAvailability;
    private final ReservationDetails reservationDetails;

    public Either<Failure, Success> confirm(UUID reservationId) {
        return roomAvailability.book(reservationId)
                .peek(success -> reservationDetails.confirm(reservationId));
    }
}
