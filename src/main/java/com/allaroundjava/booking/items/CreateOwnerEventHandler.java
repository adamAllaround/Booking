package com.allaroundjava.booking.items;

import com.allaroundjava.booking.common.events.OwnerCreatedEvent;
import org.springframework.context.event.EventListener;

public class CreateOwnerEventHandler {
    @EventListener
    void handle(OwnerCreatedEvent event) {
        System.out.println("Received event with new owher ID=" + event.getSubjectId());
    }
}
