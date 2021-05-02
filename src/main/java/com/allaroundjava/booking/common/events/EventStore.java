package com.allaroundjava.booking.common.events;

import java.util.Collection;

interface EventStore {
    void save(DomainEvent domainEvent);

    Collection<DomainEvent> getUnpublishedEvents();

    void markPublished(Collection<DomainEvent> toPublish);
}
