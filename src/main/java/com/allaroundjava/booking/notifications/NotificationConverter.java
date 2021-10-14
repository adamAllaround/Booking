package com.allaroundjava.booking.notifications;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@AllArgsConstructor
class NotificationConverter {
    private final NotificationRepository notificationRepository;
    private final MessageRepository messageRepository;

    @Scheduled(fixedDelay = 2000)
    @Transactional
    void convertToMessages() {
        Collection<Notification> toPublish = notificationRepository.allUnsent();

        List<Message> messageContents = toPublish.stream()
                .map(Notification::toMessages)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        messageContents.forEach(messageRepository::save);

        if (!toPublish.isEmpty()) {
            log.info("Attempting to publish notifications {}", toPublish);
            notificationRepository.markPublished(toPublish);
            log.info("Successfully published notifications");
        }
    }

}