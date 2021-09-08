package com.allaroundjava.booking.notifications.sending;

import com.allaroundjava.booking.notifications.Message;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class EmailSender {

    public void send(Message message) {
        log.info("Sending Email to " + message.getRecipient());
    }
}
