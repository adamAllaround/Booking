package com.allaroundjava.booking.common.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.sql.Timestamp;
import java.util.Map;

import static com.allaroundjava.booking.common.events.DatabaseEventStore.EventType.HotelRoomCreated;
import static com.allaroundjava.booking.common.events.DatabaseEventStore.EventType.OwnerCreated;

interface DbInsert {

    String getInsertStatement();

    Map<String, Object> getParams();
}

@AllArgsConstructor
class OwnerCreatedDbInsert implements DbInsert {
    private final OwnerCreatedEvent event;

    @Override
    public String getInsertStatement() {
        return "insert into Events (id, type, created, published, subjectId, payload) values (:id,:type,:created,:published,:subjectId, :payload::json)";
    }

    @Override
    public Map<String, Object> getParams() {
        return ImmutableMap.<String, Object>builder()
                .put("id", event.getEventId())
                .put("type", OwnerCreated.name())
                .put("created", Timestamp.from(event.getCreated()))
                .put("published", false)
                .put("subjectId", event.getSubjectId())
                .put("payload", "{}")
                .build();
    }
}

@AllArgsConstructor
@Log4j2
class HotelRoomCreatedDbInsert implements DbInsert {
    private final HotelRoomCreatedEvent event;
    private final ObjectMapper objectMapper;

    @Override
    public String getInsertStatement() {
        return "insert into Events (id, type, created, published, subjectId, payload) values (:id,:type,:created,:published,:subjectId, :payload::json)";
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
            log.error("Could serialize event to jason and persist event {}", event);
            throw new RuntimeException(e);
        }
    }
}
