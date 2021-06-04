package com.allaroundjava.booking.bookings.domain.model;

import com.allaroundjava.booking.bookings.domain.model.OccupationEvent.*;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.allaroundjava.booking.common.CommandResult.announceFailure;
import static com.allaroundjava.booking.common.CommandResult.announceSuccess;

@AllArgsConstructor
public class Occupation {
    private final UUID itemId;

    private final List<Booking> bookings;
    private final Availabilities availabilities;

    public Either<AddAvailabilityFailure, AddAvailabilitySuccess> addAvailability(UUID itemId, Interval interval) {
        return availabilities.tryAdd(interval)
                .<Either<AddAvailabilityFailure, AddAvailabilitySuccess>>map(
                        availabilityList -> announceSuccess(new AddAvailabilitySuccess(itemId, availabilityList)))
                .orElseGet(() -> announceFailure(new AddAvailabilityFailure(itemId, "Cannot Add Availability")));
    }

    Either<RemoveAvailabilityFailure, RemoveAvailabilitySuccess> removeAvailability(Availability availability) {
        if(availabilities.remove(availability)) {
            return announceSuccess(new RemoveAvailabilitySuccess(itemId, availability));
        }

        return announceFailure(new RemoveAvailabilityFailure(itemId, availability.getId(), "Availability does not exist"));
    }

    public Either<BookingFailure, OccupationEvent.BookingSuccess> addBooking(Interval interval) {
        Optional<Availability> availability = availabilities.findCovering(interval);

        if (availability.isEmpty()) {
            return announceFailure(new BookingFailure(itemId, interval, "Could not find suitable availability"));
        }

        Booking booking = Booking.from(availability.get());
        bookings.add(booking);
        availabilities.remove(availability.get());
        return announceSuccess(new BookingSuccess(itemId, booking));
    }

    Either<RemoveBookingFailure, RemoveBookingSuccess> removeBooking(Booking booking) {
        if (!bookings.remove(booking)) {
            return announceFailure(new RemoveBookingFailure(itemId, booking));
        }
        //TODO booking cannot carry the same ID as availability - single booking spans few availabilities
        Availability availability = Availability.from(booking);
//        availabilities.tryAdd(availability);
        return announceSuccess(new RemoveBookingSuccess(itemId, booking, availability));
    }
}
