package com.allaroundjava.booking.notifications;

import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class BookingSuccessEvent {
    UUID id;
    UUID bookingId;
    UUID itemId;
    Instant createdAt;
    boolean sent;
    Interval interval;
    int nights;
    String bookerEmail;
}
