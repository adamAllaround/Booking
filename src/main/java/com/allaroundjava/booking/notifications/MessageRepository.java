package com.allaroundjava.booking.notifications;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
class MessageRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    void save(Message message) {

        ImmutableMap<String, Object> params = ImmutableMap.of(
                "id", message.getId(),
                "eventId", message.getEventId(),
                "published", false,
                "recipient", message.getRecipient(),
                "content", message.getContent());

        jdbcTemplate.update("INSERT INTO Messages (id, eventId, sent, recipient, content) values (:id, :eventId, false, :recipient, :content)",
                params);

    }

    void markSent(Message message) {
        ImmutableMap<String, Object> params = ImmutableMap.of(
                "id", message.getId());

        jdbcTemplate.update("UPDATE Messages set sent=true where id=:id",
                params);
    }

    Collection<Message> allUnsent() {
        return jdbcTemplate.query("select * from Messages where sent = false",
                new BeanPropertyRowMapper<>(MessageDatabaseEntity.class))
                .stream()
                .map(MessageDatabaseEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Data
    @NoArgsConstructor
    public static class MessageDatabaseEntity {
        private UUID id;
        private UUID eventId;
        private boolean sent;
        private String recipient;
        private String content;

        public Message toDomain() {
            return new Message(id, eventId, recipient, content);
        }

    }
}
