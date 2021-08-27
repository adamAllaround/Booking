package com.allaroundjava.booking.notifications.sending;

import com.allaroundjava.booking.notifications.Interval;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.StringWriter;
import java.time.OffsetTime;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class BookingSuccessClientMessage implements Message {


    private final UUID bookingId;
    private final String ownerEmail;
    private final String receiverEmail;
    private final Interval interval;
    private final int nights;
    private final OffsetTime hotelHourStart;
    private final OffsetTime hotelHourEnd;

    @Override
    public void send(EmailSender sender) {
        sender.send(receiverEmail, getContent());
    }

    public String getContent() {

        try {
            Map<String, Object> params = ImmutableMap.<String, Object>builder()
                    .put("bookingId", bookingId)
                    .put("ownerEmail", ownerEmail)
                    .put("bookerEmail", receiverEmail)
                    .put("interval", interval)
                    .put("nights", nights)
                    .put("hotelHourStart", hotelHourStart)
                    .put("hotelHourEnd", hotelHourEnd)
                    .build();

            MustacheFactory mustacheFactory = new DefaultMustacheFactory();
            Mustache mustache = mustacheFactory.compile("message-templates/booking-success-booker.mustache");

            StringWriter writer = new StringWriter();
            mustache.execute(writer, params).flush();
            return writer.toString();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
