package com.allaroundjava.booking.common.events;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import javax.transaction.Transactional;
import java.util.Collection;

@AllArgsConstructor
class StoreEventPublisherDecorator implements EventPublisher{
    private final EventPublisher eventPublisher;
    private final EventStore eventStore;

    @Override
    public void publish(DomainEvent domainEvent) {
        eventStore.save(domainEvent);
    }

    @Scheduled(fixedRate = 5000L)
    @Transactional
    void publishAllPeriodically() {
        Collection<DomainEvent> toPublish = eventStore.getUnpublishedEvents();
        toPublish.forEach(eventPublisher::publish);
        eventStore.markPublished(toPublish);
    }
}
