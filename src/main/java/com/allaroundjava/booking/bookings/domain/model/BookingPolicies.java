package com.allaroundjava.booking.bookings.domain.model;

import io.vavr.control.Either;
import lombok.NonNull;
import lombok.Value;

import java.util.List;
import java.util.Optional;

import static com.allaroundjava.booking.bookings.domain.model.BookingPolicy.*;

@Value
public class BookingPolicies {
    @NonNull List<BookingPolicy> policies;

    Optional<Rejection> canBook(Availabilities availabilities) {
        return policies.stream()
                .map(policy -> policy.apply(availabilities))
                .filter(Either::isLeft)
                .map(Either::getLeft)
                .findAny();
    }

    public static BookingPolicies allHotelRoomPolicies() {
        return new BookingPolicies(List.of(canBookNonemptyAvailabilities,
                cannotBookOverBookedAvailability,
                canBookContinuousAvailabilitySet));
    }
}
