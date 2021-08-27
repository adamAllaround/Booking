package com.allaroundjava.booking.bookings.domain.model;

import com.allaroundjava.booking.common.events.DomainEvent;
import lombok.NonNull;
import lombok.Value;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface OccupationEvent extends DomainEvent {

    UUID getItemId();

    default UUID getSubjectId() {
        return getItemId();
    }

    @Value
    class AddAvailabilityFailure implements OccupationEvent {
        UUID eventId = UUID.randomUUID();
        Instant created = Instant.now();
        @NonNull UUID itemId;
        @NonNull String reason;
    }

    @Value
    class AddAvailabilitySuccess implements OccupationEvent {
        UUID eventId = UUID.randomUUID();
        Instant created = Instant.now();
        @NonNull UUID itemId;
        @NonNull List<Availability> availabilityList;
    }

    @Value
    class RemoveAvailabilitySuccess implements OccupationEvent {
        UUID eventId = UUID.randomUUID();
        Instant created = Instant.now();
        @NonNull UUID itemId;
        @NonNull Availability availability;
    }

    @Value
    class RemoveAvailabilityFailure implements OccupationEvent {
        UUID eventId = UUID.randomUUID();
        Instant created = Instant.now();
        @NonNull UUID itemId;
        @NonNull UUID availabilityId;
        @NonNull String reason;
    }

    @Value
    class BookingFailure implements OccupationEvent {
        UUID eventId = UUID.randomUUID();
        Instant created = Instant.now();
        @NonNull UUID itemId;
        @NonNull Interval interval;
        @NonNull String reason;
    }

    @Value
    class BookingSuccess implements OccupationEvent {
        @NonNull UUID eventId;
        @NonNull Instant created;
        @NonNull UUID bookingId;
        @NonNull UUID itemId;
        @NonNull Interval interval;
        @NonNull Set<UUID> availabilityIds;
        @NonNull String bookerEmail;
        static BookingSuccess now(UUID bookingId, UUID itemId, Interval interval, Set<UUID> availabilityIds, String bookerEmail) {
            return new BookingSuccess(UUID.randomUUID(), Instant.now(),bookingId, itemId, interval, availabilityIds, bookerEmail);
        }
    }

    @Value
    class RemoveBookingFailure implements OccupationEvent {
        UUID eventId = UUID.randomUUID();
        Instant created = Instant.now();
        @NonNull UUID itemId;
        @NonNull Booking booking;
    }

    @Value
    class RemoveBookingSuccess implements OccupationEvent {
        UUID eventId = UUID.randomUUID();
        Instant created = Instant.now();
        @NonNull UUID itemId;
        @NonNull Booking booking;
        @NonNull Availability availability;
    }
}
