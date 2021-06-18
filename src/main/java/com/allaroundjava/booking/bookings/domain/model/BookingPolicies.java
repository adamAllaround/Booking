package com.allaroundjava.booking.bookings.domain.model;

import io.vavr.control.Either;
import lombok.NonNull;
import lombok.Value;

import java.time.Clock;
import java.util.List;
import java.util.Optional;

import static com.allaroundjava.booking.bookings.domain.model.BookingPolicy.*;

@Value
public class BookingPolicies {
    @NonNull Clock clock;
    @NonNull List<BookingPolicy> policies;

    Optional<Rejection> canBook(Availabilities availabilities) {
        return policies.stream()
                .map(policy -> policy.apply(availabilities, clock))
                .filter(Either::isLeft)
                .map(Either::getLeft)
                .findAny();
    }

    public static BookingPolicies allHotelRoomPolicies(Clock clock) {
        return new BookingPolicies(clock, List.of(canBookNonemptyAvailabilities,
                cannotBookOverBookedAvailability,
                canBookContinuousAvailabilitySet,
                canBookOnlyFutureAvailabilities));
    }
}
