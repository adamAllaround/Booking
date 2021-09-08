package com.allaroundjava.booking.notifications;

import com.allaroundjava.booking.notifications.sending.EmailSender;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@AllArgsConstructor
class MessageSender {

    private final EmailSender sender;
    private final MessageRepository messageRepository;

    @Scheduled(fixedDelay = 2000)
    @Transactional
    void sendMessages() {
        Collection<Message> toSend = messageRepository.allUnsent();
        toSend.forEach(this::sendSingleMessage);
    }

    private void sendSingleMessage(Message message) {
        sender.send(message);
        messageRepository.markSent(message);
    }

}
