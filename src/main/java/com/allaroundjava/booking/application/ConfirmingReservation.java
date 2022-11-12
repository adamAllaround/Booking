package com.allaroundjava.booking.application;

import com.allaroundjava.booking.common.Failure;
import com.allaroundjava.booking.common.Success;
import com.allaroundjava.booking.domain.availability.RoomAvailability;
import com.allaroundjava.booking.domain.details.ReservationDetails;
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
