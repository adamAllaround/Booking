package com.allaroundjava.booking.notifications.items;

import com.allaroundjava.booking.common.events.HotelRoomCreatedEvent;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;

@Log4j2
@AllArgsConstructor
public class HotelRoomCreatedEventHandler {
    private final ItemsRepository itemsRepository;

    @EventListener
    public void handle(HotelRoomCreatedEvent event) {
        log.info("Received new OwnerCreated event {}", event);
        itemsRepository.save(new HotelRoom(event.getSubjectId(), event.getOwnerId(), event.getHotelHourStart(), event.getHotelHourEnd()));
        log.info("Persisted new owner {} details in notification module. Event Id {}", event.getSubjectId(), event.getEventId());
    }
}
