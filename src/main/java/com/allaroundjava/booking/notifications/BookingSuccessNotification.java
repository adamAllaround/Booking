package com.allaroundjava.booking.notifications;

import com.allaroundjava.booking.notifications.items.HotelRoom;
import com.allaroundjava.booking.notifications.owners.Owner;
import com.allaroundjava.booking.notifications.sending.BookingSuccessClientMessage;
import com.allaroundjava.booking.notifications.sending.BookingSuccessOwnerMessage;
import com.allaroundjava.booking.notifications.sending.Message;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Value
@Builder
public class BookingSuccessNotification implements Notification {

    UUID id;
    UUID bookingId;
    UUID itemId;
    UUID ownerId;
    String ownerEmail;
    String receiverEmail;
    Interval interval;
    int nights;
    OffsetTime hotelHourStart;
    OffsetTime hotelHourEnd;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public List<Message> toMessages() {
        return List.of(
                new BookingSuccessClientMessage(bookingId,
                        ownerEmail,
                        receiverEmail,
                        interval,
                        nights,
                        hotelHourStart,
                        hotelHourEnd),
                new BookingSuccessOwnerMessage(bookingId,
                        ownerEmail,
                        receiverEmail,
                        interval,
                        nights));
    }

    @Override
    public Notification enrichItemsData(Map<UUID, HotelRoom> items) {
        return builder()
                .bookingId(this.bookingId)
                .itemId(this.itemId)
                .ownerId(items.get(this.itemId).getOwnerId())
                .receiverEmail(this.receiverEmail)
                .interval(this.interval)
                .nights(this.nights)
                .ownerEmail(this.ownerEmail)
                .hotelHourStart(items.get(this.itemId).getHotelHourStart())
                .hotelHourEnd(items.get(this.itemId).getHotelHourEnd())
                .build();
    }

    @Override
    public Notification enrichOwnersData(Map<UUID, Owner> owners) {
        return builder()
                .bookingId(this.bookingId)
                .itemId(this.itemId)
                .receiverEmail(this.receiverEmail)
                .interval(this.interval)
                .nights(this.nights)
                .ownerEmail(owners.get(this.ownerId).getEmail())
                .hotelHourStart(this.getHotelHourStart())
                .hotelHourEnd(this.getHotelHourEnd())
                .build();
    }
}
