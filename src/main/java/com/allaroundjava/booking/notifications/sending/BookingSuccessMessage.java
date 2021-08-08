package com.allaroundjava.booking.notifications.sending;

import com.allaroundjava.booking.notifications.Interval;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class BookingSuccessMessage implements Message {


    UUID bookingId;
    String ownerEmail;
    String receiverEmail;
    Interval interval;
    int nights;

    @Override
    public void send(EmailSender sender) {


    }

    public String getContent() {

        try {
            Map<String, Object> params = ImmutableMap.<String, Object>builder()
                    .put("bookingId", bookingId)
                    .put("ownerEmail", ownerEmail)
                    .put("bookerEmail", receiverEmail)
                    .put("interval", interval)
                    .put("nights", nights)
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
