package com.allaroundjava.booking.common.events;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.time.Instant;
import java.time.OffsetTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.allaroundjava.booking.common.events.DatabaseEventStore.EventDatabaseEntity.EventType.OwnerCreated;
import static com.allaroundjava.booking.common.events.DatabaseEventStore.EventDatabaseEntity.EventType.HotelRoomCreated;

@Log4j2
@AllArgsConstructor
public class DatabaseEventStore implements EventStore {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void insert(DomainEvent domainEvent) {
        log.info("Attempting  to store event {}", domainEvent);
        if (domainEvent instanceof OwnerCreatedEvent) {
            insert(domainEvent.getEventId(), domainEvent.getCreated(), domainEvent.getSubjectId(), OwnerCreated.name());
        }
        if (domainEvent instanceof HotelRoomCreatedEvent) {
            HotelRoomCreatedEvent hotelRoomCreated = (HotelRoomCreatedEvent) domainEvent;
            insert(hotelRoomCreated.getEventId(), hotelRoomCreated.getCreated(), hotelRoomCreated.getSubjectId(),
                    HotelRoomCreated.name(), hotelRoomCreated.getHotelHourStart(), hotelRoomCreated.getHotelHourEnd());
        }
        log.info("Successfully stored event {}", domainEvent.getEventId());
    }

    private void insert(UUID eventId, Instant created, UUID subjectId, String eventType) {
        ImmutableMap<String, Object> params = ImmutableMap.of("id", eventId,
                "type", eventType,
                "created", created,
                "published", false,
                "subjectId", subjectId);
        jdbcTemplate.update("insert into Events (id, type, created, published, subjectId) values (:id,:type,:created,:published,:subjectId)",
                params);
    }
//TODO refactor - introduce param object
    private void insert(UUID eventId, Instant created, UUID subjectId, String eventType, OffsetTime hotelHourStart, OffsetTime hotelHourEnd) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", eventId);
        params.put("type", eventType);
        params.put("created", created);
        params.put("published", false);
        params.put("subjectId", subjectId);
        params.put("hotelHourStart", hotelHourStart);
        params.put("hotelHourEnd", hotelHourEnd);

        jdbcTemplate.update("insert into Events (id, type, created, published, subjectId, hotelHourStart, hotelHourEnd) values (:id,:type,:created,:published,:subjectId, :hotelHourStart, :hotelHourEnd)",
                params);
    }


    @Override
    public List<DomainEvent> getUnpublishedEvents() {
        return jdbcTemplate.query("select e.* from Events e where e.published=false", new BeanPropertyRowMapper<>(EventDatabaseEntity.class))
                .stream()
                .map(EventDatabaseEntity::toDomainEvent)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void markPublished(Collection<DomainEvent> toPublish) {
        String ids = toPublish.stream()
                .map(event -> event.getEventId().toString())
                .collect(Collectors.joining(","));
        SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);

        log.info("Marking events published {}", ids);
        jdbcTemplate.update("update Events e set e.published=true where e.id in (:ids)", parameters);
    }


    @Data
    @NoArgsConstructor
    static class EventDatabaseEntity {

        enum EventType {
            OwnerCreated,
            HotelRoomCreated
        }

        private UUID id;
        private EventType type;
        private Instant created;
        private boolean published;
        private UUID subjectId;
        private OffsetTime hotelHourStart;
        private OffsetTime hotelHourEnd;

        DomainEvent toDomainEvent() {
            switch (type) {
                case OwnerCreated:
                    return new OwnerCreatedEvent(id, created, subjectId);
                case HotelRoomCreated:
                    return new HotelRoomCreatedEvent(id, created, subjectId, hotelHourStart, hotelHourEnd);
                default:
                    throw new IllegalArgumentException("Unknown event type");
            }
        }
    }
}
