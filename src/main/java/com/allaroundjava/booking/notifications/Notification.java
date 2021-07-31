package com.allaroundjava.booking.notifications;

import java.time.Instant;
import java.util.UUID;

interface Notification {
    UUID getId();
    Instant getCreatedAt();
    boolean isSent();
}
