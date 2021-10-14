package com.allaroundjava.booking.notifications;

import com.allaroundjava.booking.notifications.items.HotelRoom;
import com.allaroundjava.booking.notifications.items.ItemsRepository;
import com.allaroundjava.booking.notifications.owners.Owner;
import com.allaroundjava.booking.notifications.owners.OwnersRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Optional;

@Log4j2
@AllArgsConstructor
public class EnrichmentVisitor {
    private final OwnersRepository ownersRepository;
    private final ItemsRepository itemsRepository;

    Optional<Notification> enrichBookingSuccess(BookingSuccessNotification bookingSuccessNotification) {

        Optional<HotelRoom> item = itemsRepository.findById(bookingSuccessNotification.getItemId());

        if (item.isEmpty()) {
            log.error("Could not find matching item for itemId {}, notificationId {}",
                    bookingSuccessNotification.getItemId(), bookingSuccessNotification.getId());
            return Optional.empty();
        }

        Optional<Owner> owner = ownersRepository.findById(item.get().getOwnerId());

        if (owner.isEmpty()) {
            log.error("Could not find matching for for ownerId {}, notificationId {}",
                    item.get().getOwnerId(), bookingSuccessNotification.getId());
            return Optional.empty();
        }
        return Optional.of(bookingSuccessNotification
                .enrichItemData(item.get())
                .enrichOwnerData(owner.get()));
    }
}
