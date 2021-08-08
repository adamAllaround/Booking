package com.allaroundjava.booking.common.events;

import com.allaroundjava.booking.bookings.domain.model.OccupationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.sql.Timestamp;
import java.util.Map;

import static com.allaroundjava.booking.common.events.DatabaseEventStore.EventType.*;

interface DbInsert {
    String EVENT_INSERT = "insert into Events (id, type, created, published, subjectId, payload) values (:id,:type,:created,:published,:subjectId, :payload::json)";

    String getInsertStatement();

    Map<String, Object> getParams();
}

@Log4j2
@AllArgsConstructor
class OwnerCreatedDbInsert implements DbInsert {
    private final OwnerCreatedEvent event;
    private final ObjectMapper objectMapper;

    @Override
    public String getInsertStatement() {
        return EVENT_INSERT;
    }

    @Override
    public Map<String, Object> getParams() {
        EventPayload.Owner eventPayload = new EventPayload.Owner();
        eventPayload.setEmail(event.getOwnerContactEmail());
        try {
        return ImmutableMap.<String, Object>builder()
                .put("id", event.getEventId())
                .put("type", OwnerCreated.name())
                .put("created", Timestamp.from(event.getCreated()))
                .put("published", false)
                .put("subjectId", event.getSubjectId())
                .put("payload", objectMapper.writeValueAsString(eventPayload))
                .build();
        } catch (JsonProcessingException e) {
            log.error("Could serialize event to json and persist owner created event {}", event);
            throw new RuntimeException(e);
        }
    }
}

@AllArgsConstructor
@Log4j2
class HotelRoomCreatedDbInsert implements DbInsert {
    private final HotelRoomCreatedEvent event;
    private final ObjectMapper objectMapper;

    @Override
    public String getInsertStatement() {
        return EVENT_INSERT;
    }

    @Override
    public Map<String, Object> getParams() {
        EventPayload.HotelRoom eventPayload = new EventPayload.HotelRoom();
        eventPayload.setHotelHourStart(event.getHotelHourStart());
        eventPayload.setHotelHourEnd(event.getHotelHourEnd());
        try {
            return ImmutableMap.<String, Object>builder()
                    .put("id", event.getEventId())
                    .put("type", HotelRoomCreated.name())
                    .put("created", Timestamp.from(event.getCreated()))
                    .put("published", false)
                    .put("subjectId", event.getSubjectId())
                    .put("payload", objectMapper.writeValueAsString(eventPayload))
                    .build();
        } catch (JsonProcessingException e) {
            log.error("Could serialize event to json and persist hotel room created event {}", event);
            throw new RuntimeException(e);
        }
    }
}

@Log4j2
@AllArgsConstructor
class BookingSuccessDbInsert implements DbInsert {
    private final OccupationEvent.BookingSuccess event;
    private final ObjectMapper objectMapper;

    @Override
    public String getInsertStatement() {
        return EVENT_INSERT;
    }

    @Override
    public Map<String, Object> getParams() {
        EventPayload.Booking eventPayload = new EventPayload.Booking();
        eventPayload.setItemId(event.getItemId());
        eventPayload.setInterval(event.getInterval());
        eventPayload.setAvailabilityIds(event.getAvailabilityIds());
        eventPayload.setBookingId(event.getBookingId());
        try {
            return ImmutableMap.<String, Object>builder()
                    .put("id", event.getEventId())
                    .put("type", BookingSuccess.name())
                    .put("created", Timestamp.from(event.getCreated()))
                    .put("published", false)
                    .put("subjectId", event.getSubjectId())
                    .put("payload", objectMapper.writeValueAsString(eventPayload))
                    .build();
        } catch (JsonProcessingException e) {
            log.error("Could serialize event to jason and persist booking success event {}", event);
            throw new RuntimeException(e);
        }
    }
}
