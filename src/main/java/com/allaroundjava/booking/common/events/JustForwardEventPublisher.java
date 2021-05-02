package com.allaroundjava.booking.common.events;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

@AllArgsConstructor
class JustForwardEventPublisher implements EventPublisher{
    private final ApplicationEventPublisher applicationEventPublisher;
    @Override
    public void publish(DomainEvent domainEvent) {
        applicationEventPublisher.publishEvent(domainEvent);
    }
}
