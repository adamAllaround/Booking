package com.allaroundjava.booking.bookings.domain.model;

import io.vavr.control.Either;
import lombok.NonNull;
import lombok.Value;

import java.util.function.Function;

import static com.allaroundjava.booking.common.CommandResult.announceFailure;
import static com.allaroundjava.booking.common.CommandResult.announceSuccess;

interface BookingPolicy extends Function<Availabilities, Either<Rejection, Allowance>> {
    BookingPolicy canBookNonemptyAvailabilities = (Availabilities availabilities) -> {
        if (availabilities.isEmpty()) {
            return announceFailure(new Rejection("Booked item is not available at that time period"));
        }
        return announceSuccess(new Allowance());
    };

    BookingPolicy cannotBookOverBookedAvailability = (Availabilities availabilities) -> {
        if (availabilities.isAnyBooked()) {
            return announceFailure(new Rejection("Item is already booked during that time period"));
        }
        return announceSuccess(new Allowance());
    };

    BookingPolicy canBookContinuousAvailabilitySet = (Availabilities availabilities) -> {
        if (!availabilities.isContinuous()) {
            return announceFailure(new Rejection("Item is not fully available through the whole period"));
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
