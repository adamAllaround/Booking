package com.allaroundjava.booking.common.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DatabaseEventStore implements EventStore {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void save(DomainEvent domainEvent) {
        jdbcTemplate.update("insert into Events (subjectId,created) values (?,?)",
                domainEvent.getSubjectId(),
                domainEvent.getCreated());

    }

    @Override
    public List<DomainEvent> getUnpublishedEvents() {
        return jdbcTemplate.queryForList("select e.* from Events e where e.published=false", EventDatabaseEntity.class)
                .stream().map(EventDatabaseEntity::toDomainEvent).collect(Collectors.toList());
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
            OwnerCreated
        }

        private UUID eventId;

        private EventType type;
        private Instant created;
        private boolean published;
        private Long subjectId;

        DomainEvent toDomainEvent() {
            switch (type) {
                case OwnerCreated:
                    return new OwnerCreatedEvent(eventId, created, subjectId);
                default:
                    throw new IllegalArgumentException("Unknown event type");
            }
        }

    }
}
