package com.allaroundjava.booking.bookings.adapters.event;

import com.allaroundjava.booking.bookings.domain.ports.ItemsRepository;
import com.allaroundjava.booking.common.events.HotelRoomCreatedEvent;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@AllArgsConstructor
class ItemCreatedEventHandler {
    private final ItemsRepository itemsRepository;

    @EventListener
    public void handle(HotelRoomCreatedEvent event) {
        log.info("Received new HotelRoomCreatedEvent {}", event);
        itemsRepository.saveNew(event.getSubjectId(), event.getCreated(), event.getHotelHourStart(), event.getHotelHourEnd());
        log.info("Persisted HotelRoomCreatedEvent {}", event.getEventId());
    }
}
