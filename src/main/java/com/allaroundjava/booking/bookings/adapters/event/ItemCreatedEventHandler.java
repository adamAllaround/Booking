package com.allaroundjava.booking.bookings.adapters.event;

import com.allaroundjava.booking.bookings.domain.model.ItemType;
import com.allaroundjava.booking.bookings.domain.ports.ItemsRepository;
import com.allaroundjava.booking.common.events.HotelRoomCreatedEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
class ItemCreatedEventHandler {
    private final ItemsRepository itemsRepository;

    @EventListener
    public void handle(HotelRoomCreatedEvent event) {
        itemsRepository.saveNew(event.getSubjectId(), event.getCreated(), ItemType.HotelRoom, event.getHotelHourStart(), event.getHotelHourEnd());
    }
}
