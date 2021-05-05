package com.allaroundjava.booking.common.events;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

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
            eventStore.markPublished(toPublish);
        }
    }
}
