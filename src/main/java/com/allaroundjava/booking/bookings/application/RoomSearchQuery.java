package com.allaroundjava.booking.bookings.application;

import com.allaroundjava.booking.bookings.shared.Interval;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class RoomSearchQuery {
    @NonNull UUID ownerId;
    @NonNull Interval searchInterval;
    @NonNull Integer capacity;
}
