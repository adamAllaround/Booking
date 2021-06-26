package com.allaroundjava.booking.common.events;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Log4j2
@AllArgsConstructor
class StoreEventPublisherDecorator implements EventPublisher {
    private final EventPublisher eventPublisher;
    private final EventStore eventStore;

    @Override
    public void publish(DomainEvent domainEvent) {
        eventStore.insert(domainEvent);
    }

    @Scheduled(fixedRate = 3000L)
    @Transactional
    void publishAllPeriodically() {
        Collection<DomainEvent> toPublish = eventStore.getUnpublishedEvents();
        toPublish.forEach(eventPublisher::publish);
        if (!toPublish.isEmpty()) {
            log.info("Attempting to publish events {}", toPublish);
            eventStore.markPublished(toPublish);
            log.info("Successfully published events");
        }
    }
}
