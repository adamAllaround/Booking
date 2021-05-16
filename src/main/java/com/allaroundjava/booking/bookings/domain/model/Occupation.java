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
public class Occupation {
    private final Item item;

    private final List<Booking> bookings;
    private final Availabilities availabilities;

    public Either<AddAvailabilityFailure, AddAvailabilitySuccess> addAvailability(Interval interval) {
        if(availabilities.overlapsExisting(interval)) {
            return announceFailure(new AddAvailabilityFailure(item.getId(), "Cannot Overlap Existing Availability"));
        }

        Availability candidate = Availability.from(interval);
        availabilities.add(candidate);
        return announceSuccess(new AddAvailabilitySuccess(item.getId(), candidate));
    }

    Either<RemoveAvailabilityFailure, RemoveAvailabilitySuccess> removeAvailability(Availability availability) {
        if(availabilities.remove(availability)) {
            return announceSuccess(new RemoveAvailabilitySuccess(item.getId(), availability.getId()));
        }

        return announceFailure(new RemoveAvailabilityFailure(item.getId(), availability.getId(), "Availability does not exist"));
    }

    public Either<BookingFailure, OccupationEvent.BookingSuccess> addBooking(Interval interval) {
        Optional<Availability> availability = availabilities.findCovering(interval);

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
