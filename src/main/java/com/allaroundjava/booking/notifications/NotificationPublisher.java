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
        //each message will produce an owner and a client message
        //unability to send an owner message cannot block client to receive it and the other way round
        //unability to send one client message cannot block other from receiving it.
        toPublish.stream().map(Notification::toMessage).forEach(message -> message.send(sender));
        if (!toPublish.isEmpty()) {
            log.info("Attempting to publish events {}", toPublish);
            repository.markPublished(toPublish);
            log.info("Successfully published events");
        }
    }
}
