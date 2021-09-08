package com.allaroundjava.booking.notifications;

import lombok.Value;

import java.util.UUID;

@Value
public class Message {
    UUID id;
    UUID eventId;
    String recipient;
    String content;
}
