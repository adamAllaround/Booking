package com.allaroundjava.booking.notifications;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.allaroundjava.booking.notifications.NotificationType.BookingSuccess;

@AllArgsConstructor
@Log4j2
class NotificationRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    void save(BookingSuccessNotification bookingSuccessNotification) {
        NotificationPayload.BookingSuccess payload = new NotificationPayload.BookingSuccess();
        payload.setOwnerEmail(bookingSuccessNotification.getOwnerEmail());
        payload.setBookerEmail(bookingSuccessNotification.getReceiverEmail());
        payload.setBookingId(bookingSuccessNotification.getBookingId());
        payload.setNights(bookingSuccessNotification.getNights());
        payload.setStart(bookingSuccessNotification.getInterval().getStart());
        payload.setEnd(bookingSuccessNotification.getInterval().getEnd());
        try {
            ImmutableMap<String, Object> params = ImmutableMap.of(
                    "id", bookingSuccessNotification.getId(),
                    "type", BookingSuccess.name(),
                    "created", Timestamp.from(bookingSuccessNotification.getCreatedAt()),
                    "sent", bookingSuccessNotification.isSent(),
                    "payload", objectMapper.writeValueAsString(payload));

            jdbcTemplate.update("INSERT INTO Notifications (id, type, created, sent, payload) values (:id, :type, :created, :sent, :payload::json)",
                    params);
        } catch (JsonProcessingException e) {
            log.error("Could serialize event to json and persist notification {}", bookingSuccessNotification);
            throw new RuntimeException(e);
        }
    }

    public Collection<Notification> all() {
        return jdbcTemplate.query("SELECT * FROM Notifications", new NotificationRowMapper(objectMapper))
                .stream()
                .map(NotificationEntity::toDomainModel)
                .collect(Collectors.toList());
    }

    Collection<Notification> allUnsent() {
        return jdbcTemplate.query("SELECT * FROM Notifications where sent=false", new NotificationRowMapper(objectMapper))
                .stream()
                .map(NotificationEntity::toDomainModel)
                .collect(Collectors.toList());
    }

    void markPublished(Collection<Notification> toPublish) {
        List<UUID> ids = toPublish.stream()
                .map(Notification::getId)
                .collect(Collectors.toList());
        SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);

        log.info("Marking notifications sent {}", ids);
        jdbcTemplate.update("UPDATE Notifications set sent=true where id in (:ids)", parameters);
    }

    @Data
    abstract static class NotificationEntity {
        String id;
        String type;
        Timestamp created;
        Boolean sent;

        abstract Notification toDomainModel();
    }

    @AllArgsConstructor
    class NotificationRowMapper implements org.springframework.jdbc.core.RowMapper<NotificationEntity> {
        private final ObjectMapper objectMapper;

        @Override
        public NotificationEntity mapRow(ResultSet resultSet, int i) throws SQLException {
            NotificationType type = NotificationType.valueOf(resultSet.getString("type"));
            if (BookingSuccess.equals(type)) {
                return new BookingSuccessNotificationEntity.RowMapper(objectMapper).mapRow(resultSet, i);
            }
            throw new IllegalArgumentException(String.format("Unknown event type %s", type));
        }
    }

    @Data
    static class BookingSuccessNotificationEntity extends NotificationEntity {

        UUID bookingId;
        String ownerEmail;
        String bookerEmail;
        Instant start;
        Instant end;
        int nights;

        @Override
        Notification toDomainModel() {
            return new BookingSuccessNotification(UUID.fromString(id),
                    bookingId,
                    created.toInstant(),
                    sent,
                    ownerEmail,
                    bookerEmail,
                    new Interval(start, end),
                    nights);
        }

        @AllArgsConstructor
        @Log4j2
        static class RowMapper implements org.springframework.jdbc.core.RowMapper<BookingSuccessNotificationEntity> {
            private final ObjectMapper objectMapper;

            @Override
            public BookingSuccessNotificationEntity mapRow(ResultSet resultSet, int i) throws SQLException {
                try {
                    BookingSuccessNotificationEntity entity = new BookingSuccessNotificationEntity();
                    entity.id = resultSet.getString("id");
                    entity.created = resultSet.getTimestamp("created");
                    entity.sent = resultSet.getBoolean("sent");
                    entity.type = resultSet.getString("type");
                    NotificationPayload.BookingSuccess payload = objectMapper.readValue(resultSet.getString("payload"), NotificationPayload.BookingSuccess.class);
                    entity.ownerEmail = payload.getOwnerEmail();
                    entity.bookerEmail = payload.getBookerEmail();
                    entity.bookingId = payload.getBookingId();
                    entity.start = payload.getStart();
                    entity.end = payload.getEnd();
                    entity.nights = payload.getNights();

                    return entity;
                } catch (JsonProcessingException e) {
                    log.error("Could not parse notification payload from json. Notifiaction Id {}, reason {}", resultSet.getObject("id").toString(), e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
