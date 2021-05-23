package com.allaroundjava.booking.common.events;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.allaroundjava.booking.common.events.DatabaseEventStore.EventDatabaseEntity.EventType.OwnerCreated;
import static com.allaroundjava.booking.common.events.DatabaseEventStore.EventDatabaseEntity.EventType.ItemCreated;

@AllArgsConstructor
public class DatabaseEventStore implements EventStore {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void insert(DomainEvent domainEvent) {
        if (domainEvent instanceof OwnerCreatedEvent) {
            insert(domainEvent.getEventId(), domainEvent.getCreated(), domainEvent.getSubjectId(), OwnerCreated.name());
        }
        if (domainEvent instanceof ItemCreatedEvent) {
            insert(domainEvent.getEventId(), domainEvent.getCreated(), domainEvent.getSubjectId(), ItemCreated.name());
        }
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

        jdbcTemplate.update("update Events e set e.published=true where e.id in (:ids)", parameters);
    }


    @Data
    @NoArgsConstructor
    static class EventDatabaseEntity {

        enum EventType {
            OwnerCreated,
            ItemCreated
        }

        private UUID id;
        private EventType type;
        private Instant created;
        private boolean published;
        private UUID subjectId;

        DomainEvent toDomainEvent() {
            switch (type) {
                case OwnerCreated:
                    return new OwnerCreatedEvent(id, created, subjectId);
                case ItemCreated:
                    return new ItemCreatedEvent(id, created, subjectId);
                default:
                    throw new IllegalArgumentException("Unknown event type");
            }
        }

    }
}
