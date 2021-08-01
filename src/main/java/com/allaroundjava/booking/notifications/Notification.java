package com.allaroundjava.booking.notifications;

import com.allaroundjava.booking.notifications.sending.Message;

import java.time.Instant;
import java.util.UUID;

interface Notification {
    UUID getId();
    Instant getCreatedAt();
    boolean isSent();

    Message toMessage();
}
