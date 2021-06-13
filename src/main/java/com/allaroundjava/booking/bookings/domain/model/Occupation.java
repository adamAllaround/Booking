package com.allaroundjava.booking.bookings.domain.model;

import com.allaroundjava.booking.bookings.domain.model.OccupationEvent.*;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
        if (availabilities.remove(availability)) {
            return announceSuccess(new RemoveAvailabilitySuccess(itemId, availability));
        }

        return announceFailure(new RemoveAvailabilityFailure(itemId, availability.getId(), "Availability does not exist"));
    }

    public Either<BookingFailure, OccupationEvent.BookingSuccess> addBooking(Booking booking) {

        Set<Availability> coveringAvailabilities = availabilities.matchingIds(booking.getAvailabilityIds());

        if (coveringAvailabilities.isEmpty()) {
            return announceFailure(new BookingFailure(itemId, booking.getInterval(), "Could not find suitable availability"));
        }

        if(coveringAvailabilities.stream().anyMatch(Availability::isBooked)){
            return announceFailure(new BookingFailure(itemId, booking.getInterval(), "Cannot add booking to cover booked availability"));
        }



        Set<BookedAvailability> bookedAvailabilities = coveringAvailabilities.stream()
                .map(availability -> availability.book(booking.getId()))
                .collect(Collectors.toSet());

        coveringAvailabilities.forEach(availabilities::remove);
        availabilities.addAll(bookedAvailabilities);

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
