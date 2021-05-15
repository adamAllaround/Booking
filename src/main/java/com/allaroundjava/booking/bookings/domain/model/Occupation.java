package com.allaroundjava.booking.bookings.domain.model;

import com.allaroundjava.booking.bookings.domain.model.OccupationEvent.AddAvailabilityFailure;
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent.AddAvailabilitySuccess;
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent.RemoveAvailabilityFailure;
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent.RemoveAvailabilitySuccess;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
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

    void addBooking(Duration duration) {

    }

    void removeBooking(Duration duration) {

    }
}
