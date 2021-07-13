package com.allaroundjava.booking.common.events;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

import static com.allaroundjava.booking.common.events.DatabaseEventStore.EventDatabaseEntity.EventType.HotelRoomCreated;
import static com.allaroundjava.booking.common.events.DatabaseEventStore.EventDatabaseEntity.EventType.OwnerCreated;

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
                "created", Timestamp.from(created),
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
        params.put("created", Timestamp.from(created));
        params.put("published", false);
        params.put("subjectId", subjectId);
        params.put("hotelHourStart", Time.valueOf(hotelHourStart.withOffsetSameInstant(ZoneOffset.UTC).toLocalTime()));
        params.put("hotelHourEnd", Time.valueOf(hotelHourEnd.withOffsetSameInstant(ZoneOffset.UTC).toLocalTime()));

        jdbcTemplate.update("insert into Events (id, type, created, published, subjectId, hotelHourStart, hotelHourEnd) values (:id,:type,:created,:published,:subjectId, :hotelHourStart, :hotelHourEnd)",
                params);
    }


    @Override
    public List<DomainEvent> getUnpublishedEvents() {
        return jdbcTemplate.query("select e.* from Events e where e.published=false", new EventDatabaseEntity.RowMapper())
                .stream()
                .map(EventDatabaseEntity::toDomainEvent)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void markPublished(Collection<DomainEvent> toPublish) {
        List<UUID> ids = toPublish.stream()
                .map(DomainEvent::getEventId)
                .collect(Collectors.toList());
        SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);

        log.info("Marking events published {}", ids);
        jdbcTemplate.update("update Events set published=true where id in (:ids)", parameters);
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

        static class RowMapper implements org.springframework.jdbc.core.RowMapper<EventDatabaseEntity> {

            @Override
            public EventDatabaseEntity mapRow(ResultSet resultSet, int i) throws SQLException {
                EventDatabaseEntity entity = new EventDatabaseEntity();
                entity.id = UUID.fromString(resultSet.getObject("id").toString());
                entity.type = EventType.valueOf(resultSet.getString("type"));
                entity.created = resultSet.getTimestamp("created").toInstant();
                entity.published = resultSet.getBoolean("published");
                entity.subjectId = UUID.fromString(resultSet.getObject("subjectId").toString());
                entity.hotelHourStart = offsetTime(resultSet, "hotelHourStart");
                entity.hotelHourEnd = offsetTime(resultSet, "hotelHourEnd");

                return entity;
            }

            private OffsetTime offsetTime(ResultSet resultSet, String column) throws SQLException {
                return Optional.ofNullable(resultSet.getTime(column)).map(Time::toLocalTime)
                        .map(localTime -> OffsetTime.of(localTime, ZoneOffset.UTC)).orElse(null);
            }
        }
    }
}
