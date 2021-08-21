package com.allaroundjava.booking.common.events;

import com.allaroundjava.booking.bookings.domain.model.Interval;
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.OffsetTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.allaroundjava.booking.common.events.DatabaseEventStore.EventType.*;

@Log4j2
@AllArgsConstructor
public class DatabaseEventStore implements EventStore {

    enum EventType {
        OwnerCreated,
        HotelRoomCreated,
        BookingSuccess
    }

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;
    private final DbInsertFactory dbInsertFactory;

    @Override
    public void insert(DomainEvent domainEvent) {
        log.info("Attempting  to store event {}", domainEvent);
        DbInsert insert = dbInsertFactory.get(domainEvent);
        jdbcTemplate.update(insert.getInsertStatement(), insert.getParams());

        log.info("Successfully stored event {}", domainEvent.getEventId());
    }

    @Override
    public List<DomainEvent> getUnpublishedEvents() {
        return jdbcTemplate.query("select * from Events where published=false", new EventRowMapper(objectMapper))
                .stream()
                .map(EventEntity::toDomainEvent)
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

    abstract static class EventEntity {
        abstract DomainEvent toDomainEvent();
    }

    @AllArgsConstructor
    class EventRowMapper implements org.springframework.jdbc.core.RowMapper<EventEntity> {
        private final ObjectMapper objectMapper;

        @Override
        public EventEntity mapRow(ResultSet resultSet, int i) throws SQLException {
            EventType type = EventType.valueOf(resultSet.getString("type"));
            if (OwnerCreated.equals(type)) {
                return new OwnerCreatedEventEntity.RowMapper(objectMapper).mapRow(resultSet, i);
            } else if (HotelRoomCreated.equals(type)) {
                return new HotelRoomCreatedEventEntity.RowMapper(objectMapper).mapRow(resultSet, i);
            } else if (BookingSuccess.equals(type)) {
                return new BookingSuccessEventEntity.RowMapper(objectMapper).mapRow(resultSet, i);
            }
            throw new IllegalArgumentException(String.format("Unknown event type %s", type));
        }
    }

    @Data
    @NoArgsConstructor
    static class OwnerCreatedEventEntity extends EventEntity {
        private UUID id;
        private Instant created;
        private boolean published;
        private UUID subjectId;
        private String email;

        DomainEvent toDomainEvent() {
            return new OwnerCreatedEvent(id, created, subjectId, email);
        }

        @AllArgsConstructor
        static class RowMapper implements org.springframework.jdbc.core.RowMapper<OwnerCreatedEventEntity> {
            private final ObjectMapper objectMapper;

            @Override
            public OwnerCreatedEventEntity mapRow(ResultSet resultSet, int i) throws SQLException {
                try {
                OwnerCreatedEventEntity entity = new OwnerCreatedEventEntity();
                entity.id = UUID.fromString(resultSet.getObject("id").toString());
                entity.created = resultSet.getTimestamp("created").toInstant();
                entity.published = resultSet.getBoolean("published");
                entity.subjectId = UUID.fromString(resultSet.getObject("subjectId").toString());
                EventPayload.Owner payload = objectMapper.readValue(resultSet.getString("payload"), EventPayload.Owner.class);
                entity.email = payload.getEmail();

                return entity;
                } catch (JsonProcessingException e) {
                    log.error("Could not parse event payload from json. Event Id {}, reason {}", resultSet.getObject("id").toString(), e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Data
    @NoArgsConstructor
    static class HotelRoomCreatedEventEntity extends EventEntity {

        private UUID id;
        private Instant created;
        private boolean published;
        private UUID subjectId;
        private OffsetTime hotelHourStart;
        private OffsetTime hotelHourEnd;
        private UUID ownerId;

        DomainEvent toDomainEvent() {
            return new HotelRoomCreatedEvent(id, ownerId, created, subjectId, hotelHourStart, hotelHourEnd);
        }

        @AllArgsConstructor
        static class RowMapper implements org.springframework.jdbc.core.RowMapper<HotelRoomCreatedEventEntity> {

            private final ObjectMapper objectMapper;

            @Override
            public HotelRoomCreatedEventEntity mapRow(ResultSet resultSet, int i) throws SQLException {
                try {
                    HotelRoomCreatedEventEntity entity = new HotelRoomCreatedEventEntity();
                    entity.id = UUID.fromString(resultSet.getObject("id").toString());
                    entity.created = resultSet.getTimestamp("created").toInstant();
                    entity.published = resultSet.getBoolean("published");
                    entity.subjectId = UUID.fromString(resultSet.getObject("subjectId").toString());
                    EventPayload.HotelRoom payload = objectMapper.readValue(resultSet.getString("payload"), EventPayload.HotelRoom.class);
                    entity.hotelHourStart = payload.getHotelHourStart();
                    entity.hotelHourEnd = payload.getHotelHourEnd();
                    entity.ownerId = payload.getOwnerId();

                    return entity;
                } catch (JsonProcessingException e) {
                    log.error("Could not parse event payload from json. Event Id {}, reason {}", resultSet.getObject("id").toString(), e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Data
    @NoArgsConstructor
    static class BookingSuccessEventEntity extends EventEntity {

        private UUID id;
        private Instant created;
        private boolean published;
        private UUID bookingId;
        private UUID itemId;
        private Interval interval;
        private Set<UUID> availabilityIds;

        DomainEvent toDomainEvent() {
            return new OccupationEvent.BookingSuccess(id, created, bookingId, itemId, interval, availabilityIds);
        }

        @AllArgsConstructor
        @Log4j2
        static class RowMapper implements org.springframework.jdbc.core.RowMapper<BookingSuccessEventEntity> {

            private final ObjectMapper objectMapper;

            @Override
            public BookingSuccessEventEntity mapRow(ResultSet resultSet, int i) throws SQLException {
                try {
                    BookingSuccessEventEntity entity = new BookingSuccessEventEntity();
                    entity.id = UUID.fromString(resultSet.getObject("id").toString());
                    entity.created = resultSet.getTimestamp("created").toInstant();
                    entity.published = resultSet.getBoolean("published");
                    EventPayload.Booking payload = objectMapper.readValue(resultSet.getString("payload"), EventPayload.Booking.class);
                    entity.itemId = payload.getItemId();
                    entity.interval = payload.getInterval();
                    entity.availabilityIds = payload.getAvailabilityIds();
                    entity.bookingId = payload.getBookingId();

                    return entity;
                } catch (JsonProcessingException e) {
                    log.error("Could not parse event payload from json. Event Id {}, reason {}", resultSet.getObject("id").toString(), e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        }
    }
}