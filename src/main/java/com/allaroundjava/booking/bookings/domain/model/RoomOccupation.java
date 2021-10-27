package com.allaroundjava.booking.bookings.domain.model;

import com.allaroundjava.booking.common.Entity;
import io.vavr.control.Either;
import lombok.extern.log4j.Log4j2;

import java.util.Optional;
import java.util.UUID;

import static com.allaroundjava.booking.common.CommandResult.announceFailure;
import static com.allaroundjava.booking.common.CommandResult.announceSuccess;

@Log4j2
public class RoomOccupation extends Entity {
    private final Availabilities availabilities;
    private final Bookings bookings;
    private final BookingPolicies bookingPolicies;

    public RoomOccupation(UUID id, Availabilities availabilities, Bookings bookings, BookingPolicies bookingPolicies) {
        super(id);
        this.availabilities = availabilities;
        this.bookings = bookings;
        this.bookingPolicies = bookingPolicies;
    }

    public Either<OccupationEvent.AddAvailabilityFailure, OccupationEvent.AddAvailabilitySuccess> addAvailability(Interval interval) {
        log.info("Attempting to add new availabilities for item {} and interval {}", getId(), interval);
        return availabilities.tryAdd(interval)
                .<Either<OccupationEvent.AddAvailabilityFailure, OccupationEvent.AddAvailabilitySuccess>>map(
                        availabilityList -> announceSuccess(new OccupationEvent.AddAvailabilitySuccess(getId(), availabilityList)))
                .orElseGet(() -> announceFailure(new OccupationEvent.AddAvailabilityFailure(getId(), "Cannot Add Availability")));
    }

    Either<OccupationEvent.RemoveAvailabilityFailure, OccupationEvent.RemoveAvailabilitySuccess> removeAvailability(Availability availability) {
        if (availabilities.remove(availability)) {
            return announceSuccess(new OccupationEvent.RemoveAvailabilitySuccess(getId(), availability));
        }

        return announceFailure(new OccupationEvent.RemoveAvailabilityFailure(getId(), availability.getId(), "Availability does not exist"));
    }

    public Either<OccupationEvent.BookingFailure, OccupationEvent.BookingSuccess> addBooking(Booking booking) {

        Availabilities coveringAvailabilities = availabilities.matchingIds(booking.getAvailabilityIds());

        Optional<Rejection> rejections = bookingPolicies.canBook(coveringAvailabilities);

        if (rejections.isEmpty()) {
            bookAvailabilities(booking, coveringAvailabilities);
            return announceSuccess(OccupationEvent.BookingSuccess.now(booking.getId(),
                    getId(),
                    booking.getInterval(),
                    booking.getAvailabilityIds(),
                    booking.getEmail()));
        }
        log.warn("Cannot allow to book request {}. Reason {}",booking.getId(), rejections.get().getReason());
        return announceFailure(new OccupationEvent.BookingFailure(getId(), booking.getInterval(), rejections.get().getReason()));
    }

    private void bookAvailabilities(Booking booking, Availabilities coveringAvailabilities) {
        Availabilities booked = coveringAvailabilities.bookAll(booking.getId());
        availabilities.removeAll(coveringAvailabilities);
        availabilities.addAll(booked);
    }

    Either<OccupationEvent.RemoveBookingFailure, OccupationEvent.RemoveBookingSuccess> removeBooking(Booking booking) {
        if (!bookings.remove(booking)) {
            return announceFailure(new OccupationEvent.RemoveBookingFailure(getId(), booking));
        }
        //TODO booking cannot carry the same ID as availability - single booking spans few availabilities
        Availability availability = Availability.from(booking);
//        availabilities.tryAdd(availability);
        return announceSuccess(new OccupationEvent.RemoveBookingSuccess(getId(), booking, availability));
    }
}
