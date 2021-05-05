package com.allaroundjava.booking.common.events;

import java.util.Collection;

interface EventStore {
    void insert(DomainEvent domainEvent);

    Collection<DomainEvent> getUnpublishedEvents();

    void markPublished(Collection<DomainEvent> toPublish);
}
