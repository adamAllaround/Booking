package com.allaroundjava.booking.notifications.sending;

import com.allaroundjava.booking.notifications.Interval;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class BookingSuccessOwnerMessageContent implements MessageContent {
    private final UUID bookingId;
    private final String ownerEmail;
    private final String receiverEmail;
    private final Interval interval;
    private final int nights;

    @Override
    public String getContent() {
        return "empty";
    }
}
