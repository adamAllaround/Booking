package com.allaroundjava.booking.bookings.domain.model;

import com.allaroundjava.booking.bookings.domain.model.OccupationEvent.*;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.allaroundjava.booking.common.CommandResult.announceFailure;
import static com.allaroundjava.booking.common.CommandResult.announceSuccess;

@AllArgsConstructor
class Occupation {
    private final Item item;

    private final List<Availability> availabilities = new ArrayList<>();
    private final List<Booking> bookings = new ArrayList<>();

    Either<AddAvailabilityFailure, AddAvailabilitySuccess> addAvailability(Interval interval) {
        Availability candidate = Availability.from(interval);

        Optional<Availability> rejection = availabilities.stream()
                .filter(avail -> avail.overlaps(candidate))
                .findFirst();

        if(rejection.isPresent()) {
            return announceFailure(new AddAvailabilityFailure(item.getId(), "Cannot Overlap Existing Availability"));
        }

        availabilities.add(candidate);
        return announceSuccess(new AddAvailabilitySuccess(item.getId(), candidate));
    }

    Either<RemoveAvailabilityFailure, RemoveAvailabilitySuccess> removeAvailability(Availability availability) {
        if(availabilities.remove(availability)) {
            return announceSuccess(new RemoveAvailabilitySuccess(item.getId(), availability.getId()));
        }

        return announceFailure(new RemoveAvailabilityFailure(item.getId(), availability.getId(), "Availability does not exist"));
    }

    Either<BookingFailure, OccupationEvent.BookingSuccess> addBooking(Interval interval) {
        Optional<Availability> availability = availabilities.stream()
                .filter(avail -> avail.covers(interval))
                .findFirst();

        if (availability.isEmpty()) {
            return announceFailure(new BookingFailure(item.getId(), interval, "Could not find suitable availability"));
        }

        Booking booking = Booking.from(availability.get());
        bookings.add(booking);
        availabilities.remove(availability.get());
        return announceSuccess(new BookingSuccess(item.getId(), booking));
    }

    Either<RemoveBookingFailure, RemoveBookingSuccess> removeBooking(Booking booking) {
        if (!bookings.remove(booking)) {
            return announceFailure(new RemoveBookingFailure(item.getId(), booking));
        }
        //TODO booking cannot carry the same ID as availability - single booking spans few availabilities
        Availability availability = Availability.from(booking);
        availabilities.add(availability);
        return announceSuccess(new RemoveBookingSuccess(item.getId(), booking, availability));
    }
}
