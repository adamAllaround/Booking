package com.allaroundjava.booking.notifications;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class NotificationRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;
    private final EnrichmentVisitor enrichmentVisitor;

    List<Notification> allUnsent() {

        return queryCollection("select * from NotificationEvents where sent = false");
    }

    private List<Notification> queryCollection(String sql) {
        return jdbcTemplate.query(sql,
                new EventRepository.EventRowMapper(objectMapper))
                .stream()
                .map(EventRepository.EventEntity::toNotification)
                .map(partialNotification -> partialNotification.enrich(enrichmentVisitor))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    public Optional<Notification> getSingle(UUID uuid) {
        ImmutableMap<String, UUID> params = ImmutableMap.of("id", uuid);
        return Try.of(() -> queryForSingle(params))
                .getOrElseGet((throwable) -> Optional.empty());
    }

    private Optional<Notification> queryForSingle(ImmutableMap<String, UUID> params) {
        return jdbcTemplate.queryForObject("select * from NotificationEvents where id=:notificationId",
                params,
                new EventRepository.EventRowMapper(objectMapper))
                .toNotification()
                .enrich(enrichmentVisitor);
    }

    void markPublished(Collection<Notification> toPublish) {
        ImmutableMap<String, Object> params = ImmutableMap.of("ids", toPublish.stream()
                .map(Notification::getId)
                .collect(Collectors.toList()));
        jdbcTemplate.update("UPDATE NotificationEvents set sent=true where id in (:ids)", params);
    }
}
