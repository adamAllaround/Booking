package com.allaroundjava.booking.notifications;

import com.allaroundjava.booking.notifications.items.HotelRoom;
import com.allaroundjava.booking.notifications.owners.Owner;
import com.allaroundjava.booking.notifications.sending.BookingSuccessClientMessageContent;
import com.allaroundjava.booking.notifications.sending.BookingSuccessOwnerMessageContent;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetTime;
import java.util.List;
import java.util.Optional;
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
                new Message(UUID.randomUUID(),
                        id,
                        receiverEmail,
                        new BookingSuccessClientMessageContent(bookingId,
                        ownerEmail,
                        receiverEmail,
                        interval,
                        nights,
                        hotelHourStart,
                        hotelHourEnd).getContent()),
                new Message(UUID.randomUUID(),
                        id,
                        ownerEmail,
                        new BookingSuccessOwnerMessageContent(bookingId,
                        ownerEmail,
                        receiverEmail,
                        interval,
                        nights).getContent()));
    }

    @Override
    public Optional<Notification> enrich(EnrichmentVisitor visitor) {
        return visitor.enrichBookingSuccess(this);
    }

    @Override
    public Notification enrichOwnerData(Owner owner) {
        return builder()
                .id(this.id)
                .bookingId(this.bookingId)
                .itemId(this.itemId)
                .receiverEmail(this.receiverEmail)
                .interval(this.interval)
                .nights(this.nights)
                .ownerEmail(owner.getEmail())
                .hotelHourStart(this.getHotelHourStart())
                .hotelHourEnd(this.getHotelHourEnd())
                .build();
    }

    @Override
    public Notification enrichItemData(HotelRoom item) {
        return builder()
                .id(this.id)
                .bookingId(this.bookingId)
                .itemId(this.itemId)
                .ownerId(item.getOwnerId())
                .receiverEmail(this.receiverEmail)
                .interval(this.interval)
                .nights(this.nights)
                .ownerEmail(this.ownerEmail)
                .hotelHourStart(item.getHotelHourStart())
                .hotelHourEnd(item.getHotelHourEnd())
                .build();
    }
}
