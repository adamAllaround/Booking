package com.allaroundjava.booking.notifications.sending;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class EmailSender {

    void send(String recipientEmail, String content) {
        log.info("Sending Email to " + recipientEmail);
    }
}
