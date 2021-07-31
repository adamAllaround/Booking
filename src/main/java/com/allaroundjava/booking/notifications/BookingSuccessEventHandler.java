package com.allaroundjava.booking.notifications;

import com.allaroundjava.booking.bookings.domain.model.OccupationEvent;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;

import java.time.Instant;

@Log4j2
@AllArgsConstructor
class BookingSuccessEventHandler {
    private final NotificationRepository repository;

    @EventListener
    public void handle(OccupationEvent.BookingSuccess event) {
        log.info("Received new BookingSuccess event {}", event);
        repository.save(new BookingSuccessNotification(event.getEventId(), event.getBookingId(), Instant.now(),
                false, "ownerEmail", "receiverEmail"));
        log.info("Persisted Booking Success notification request {}", event.getEventId());
    }
}
