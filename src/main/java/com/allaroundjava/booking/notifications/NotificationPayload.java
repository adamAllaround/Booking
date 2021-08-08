package com.allaroundjava.booking.notifications;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

class NotificationPayload {
    @Data
    static class BookingSuccess {
        UUID bookingId;
        String ownerEmail;
        String bookerEmail;
        int nights;
        Instant start;
        Instant end;
    }
}
