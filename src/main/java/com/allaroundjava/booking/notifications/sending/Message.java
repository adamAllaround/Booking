package com.allaroundjava.booking.notifications.sending;

public interface Message {
    void send(EmailSender sender);

    String getContent();
}
