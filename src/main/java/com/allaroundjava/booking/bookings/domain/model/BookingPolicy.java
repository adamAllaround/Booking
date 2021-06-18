package com.allaroundjava.booking.bookings.domain.model;

import io.vavr.control.Either;
import lombok.NonNull;
import lombok.Value;

import java.time.Clock;
import java.time.Instant;
import java.util.function.BiFunction;

import static com.allaroundjava.booking.common.CommandResult.announceFailure;
import static com.allaroundjava.booking.common.CommandResult.announceSuccess;

interface BookingPolicy extends BiFunction<Availabilities,Clock, Either<Rejection, Allowance>> {
    BookingPolicy canBookNonemptyAvailabilities = (Availabilities availabilities, Clock clock) -> {
        if (availabilities.isEmpty()) {
            return announceFailure(new Rejection("Booked item is not available at that time period"));
        }
        return announceSuccess(new Allowance());
    };

    BookingPolicy cannotBookOverBookedAvailability = (Availabilities availabilities, Clock clock) -> {
        if (availabilities.isAnyBooked()) {
            return announceFailure(new Rejection("Item is already booked during that time period"));
        }
        return announceSuccess(new Allowance());
    };

    BookingPolicy canBookContinuousAvailabilitySet = (Availabilities availabilities, Clock clock) -> {
        if (!availabilities.isContinuous()) {
            return announceFailure(new Rejection("Item is not fully available through the whole period"));
        }
        return announceSuccess(new Allowance());
    };

    BookingPolicy canBookOnlyFutureAvailabilities = (Availabilities availabilities, Clock clock) -> {
        if (availabilities.anyEndsBefore(Instant.now(clock))) {
            return announceFailure(new Rejection("Cannot book in the past"));
        }

        return announceSuccess(new Allowance());
    };
}

@Value
class Allowance {
}

@Value
class Rejection {
    @NonNull String reason;
}
