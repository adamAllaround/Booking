package com.allaroundjava.booking.common.events;

public interface EventPublisher {
    void publish(DomainEvent domainEvent);
}
