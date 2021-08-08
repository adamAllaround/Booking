package com.allaroundjava.booking.notifications;

import com.allaroundjava.booking.notifications.sending.BookingSuccessMessage;
import com.allaroundjava.booking.notifications.sending.Message;
import lombok.Value;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Value
public class BookingSuccessNotification implements Notification {
    UUID id;
    UUID bookingId;
    Instant createdAt;
    boolean sent;
    String ownerEmail;
    String receiverEmail;
    Interval interval;
    int nights;

    @Override
    public Message toMessage() {
        return new BookingSuccessMessage(bookingId, ownerEmail, receiverEmail, interval, nights);
    }
}
