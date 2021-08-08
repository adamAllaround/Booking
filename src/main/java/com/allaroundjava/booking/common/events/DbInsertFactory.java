package com.allaroundjava.booking.common.events;

import com.allaroundjava.booking.bookings.domain.model.OccupationEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class DbInsertFactory {
    private final ObjectMapper objectMapper;
    DbInsert get(DomainEvent domainEvent) {
        if (domainEvent instanceof OwnerCreatedEvent) {
            return new OwnerCreatedDbInsert((OwnerCreatedEvent) domainEvent, objectMapper);
        }
        if (domainEvent instanceof HotelRoomCreatedEvent) {
            return new HotelRoomCreatedDbInsert((HotelRoomCreatedEvent) domainEvent, objectMapper);
        }
        if(domainEvent instanceof OccupationEvent.BookingSuccess) {
            return new BookingSuccessDbInsert((OccupationEvent.BookingSuccess) domainEvent, objectMapper);
        }
        throw new RuntimeException("Event type is not supported " + domainEvent.getClass().getSimpleName());
    }
}
