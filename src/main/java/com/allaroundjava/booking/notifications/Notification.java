package com.allaroundjava.booking.notifications;

import com.allaroundjava.booking.notifications.items.HotelRoom;
import com.allaroundjava.booking.notifications.owners.Owner;
import com.allaroundjava.booking.notifications.sending.Message;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

interface Notification {
    UUID getId();

    List<Message> toMessages();

    Notification enrichItemsData(Map<UUID, HotelRoom> items);

    Notification enrichOwnersData(Map<UUID, Owner> owners);
}
