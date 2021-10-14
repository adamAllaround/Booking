package com.allaroundjava.booking.notifications;

import com.allaroundjava.booking.notifications.items.HotelRoom;
import com.allaroundjava.booking.notifications.owners.Owner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface Notification {
    UUID getId();

    List<Message> toMessages();

    Optional<Notification> enrich(EnrichmentVisitor visitor);
    Notification enrichOwnerData(Owner owner);
    Notification enrichItemData(HotelRoom item);
}
