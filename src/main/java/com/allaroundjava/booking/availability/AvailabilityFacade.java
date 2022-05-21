package com.allaroundjava.booking.availability;

import com.allaroundjava.booking.bookings.domain.model.Interval;
import io.vavr.control.Either;

import java.util.UUID;

public class AvailabilityFacade {
    boolean isAvailable(UUID roomId, Interval interval) {
        return false;
    }

    Either<Allowance, Rejection> makeUnavailable(UUID roomId, Interval interval) {
        return null;
    }

    Either<Allowance, Rejection> makeAvailable(UUID roomId, Interval interval) {
        return null;
    }
}
