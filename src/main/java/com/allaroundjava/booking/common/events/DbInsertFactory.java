package com.allaroundjava.booking.common.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class DbInsertFactory {
    private final ObjectMapper objectMapper;
    DbInsert get(DomainEvent domainEvent) {
        if (domainEvent instanceof OwnerCreatedEvent) {
            return new OwnerCreatedDbInsert((OwnerCreatedEvent) domainEvent);
        }
        if (domainEvent instanceof HotelRoomCreatedEvent) {
            return new HotelRoomCreatedDbInsert((HotelRoomCreatedEvent) domainEvent, objectMapper);
        }
        throw new RuntimeException("Event type is not supported " + domainEvent.getClass().getSimpleName());
    }
}
