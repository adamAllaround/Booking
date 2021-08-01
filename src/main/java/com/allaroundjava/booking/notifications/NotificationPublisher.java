package com.allaroundjava.booking.notifications;

import com.allaroundjava.booking.notifications.sending.EmailSender;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Log4j2
@AllArgsConstructor
class NotificationPublisher {
    private final NotificationRepository repository;
    private final EmailSender sender;


    @Scheduled(fixedRate = 10000L)
    @Transactional
    void publishAllPeriodically() {
        Collection<Notification> toPublish = repository.allUnsent();
        toPublish.stream().map(Notification::toMessage).forEach(message -> message.send(sender));
        if (!toPublish.isEmpty()) {
            log.info("Attempting to publish events {}", toPublish);
            repository.markPublished(toPublish);
            log.info("Successfully published events");
        }
    }
}