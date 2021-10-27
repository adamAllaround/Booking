package com.allaroundjava.booking.bookings.domain.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Availability implements Comparable<Availability> {
    @EqualsAndHashCode.Include
    UUID id;//TODO can we remove the id from here ?
    UUID itemId; //and this one too ?
    Interval interval;

    public static Availability from(UUID itemId, Interval interval) {
        return new Availability(UUID.randomUUID(), itemId, interval);
    }

    static Availability from(Booking booking) {
        return new Availability(booking.getId(), booking.getItemId(), booking.getInterval());
    }

    boolean overlaps(Availability candidate) {
        return interval.overlaps(candidate.getInterval());
    }

    boolean overlaps(Interval candidate) {
        return interval.overlaps(candidate);
    }

    public Instant getStart() {
        return interval.getStart();
    }

    public Instant getEnd() {
        return interval.getEnd();
    }

    public boolean isBooked() {
        return false;
    }

    public BookedAvailability book(UUID bookingId) {
        return new BookedAvailability(id, itemId, interval, bookingId);
    }

    @Override
    public int compareTo(Availability other) {
        return this.interval.getStart().compareTo(other.interval.getStart());
    }
}

class BookedAvailability extends Availability {
    UUID bookingId;

    public BookedAvailability(UUID id, UUID itemId, Interval interval, UUID bookingId) {
        super(id, itemId, interval);
        this.bookingId = bookingId;
    }

    @Override
    public boolean isBooked() {
        return true;
    }

    @Override
    public BookedAvailability book(UUID bookingId) {
        throw new IllegalStateException("Cannot book already booked availability");
    }
}