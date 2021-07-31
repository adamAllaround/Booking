package com.allaroundjava.booking.notifications;

import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class BookingSuccessNotification implements Notification {
    UUID id;
    UUID bookingId;
    Instant createdAt;
    boolean sent;
    String ownerEmail;
    String receiverEmail;

}
