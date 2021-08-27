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
class EventRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    void save(BookingSuccessEvent bookingSuccessEvent) {
        NotificationPayload.BookingSuccess payload = new NotificationPayload.BookingSuccess();
        payload.setBookingId(bookingSuccessEvent.getBookingId());
        payload.setItemId(bookingSuccessEvent.getItemId());
        payload.setNights(bookingSuccessEvent.getNights());
        payload.setStart(bookingSuccessEvent.getInterval().getStart());
        payload.setEnd(bookingSuccessEvent.getInterval().getEnd());
        payload.setBookerEmail(bookingSuccessEvent.getBookerEmail());
        try {
            ImmutableMap<String, Object> params = ImmutableMap.of(
                    "id", bookingSuccessEvent.getId(),
                    "type", BookingSuccess.name(),
                    "created", Timestamp.from(bookingSuccessEvent.getCreatedAt()),
                    "sent", false,
                    "payload", objectMapper.writeValueAsString(payload));

            jdbcTemplate.update("INSERT INTO NotificationEvents (id, type, created, sent, payload) values (:id, :type, :created, :sent, :payload::json)",
                    params);
        } catch (JsonProcessingException e) {
            log.error("Could serialize event to json and persist notification {}", bookingSuccessEvent);
            throw new RuntimeException(e);
        }
    }

    @Data
    abstract static class EventEntity {
        String id;
        String type;
        Timestamp created;
        Boolean sent;

        abstract Notification toNotification();
    }

    @AllArgsConstructor
    static class EventRowMapper implements org.springframework.jdbc.core.RowMapper<EventEntity> {
        private final ObjectMapper objectMapper;

        @Override
        public EventEntity mapRow(ResultSet resultSet, int i) throws SQLException {
            NotificationType type = NotificationType.valueOf(resultSet.getString("type"));
            if (BookingSuccess.equals(type)) {
                return new BookingSuccessEventEntity.RowMapper(objectMapper).mapRow(resultSet, i);
            }
            throw new IllegalArgumentException(String.format("Unknown event type %s", type));
        }
    }

    @Data
    static class BookingSuccessEventEntity extends EventEntity {

        UUID bookingId;
        UUID itemId;
        String bookerEmail;
        Instant start;
        Instant end;
        int nights;

        @Override
        Notification toNotification() {
            return BookingSuccessNotification.builder()
                    .id(UUID.fromString(id))
                    .bookingId(bookingId)
                    .itemId(itemId)
                    .receiverEmail(bookerEmail)
                    .nights(nights)
                    .interval(new Interval(start, end))
                    .build();
        }

        @AllArgsConstructor
        @Log4j2
        static class RowMapper implements org.springframework.jdbc.core.RowMapper<BookingSuccessEventEntity> {
            private final ObjectMapper objectMapper;

            @Override
            public BookingSuccessEventEntity mapRow(ResultSet resultSet, int i) throws SQLException {
                try {
                    BookingSuccessEventEntity entity = new BookingSuccessEventEntity();
                    entity.id = resultSet.getString("id");
                    entity.created = resultSet.getTimestamp("created");
                    entity.sent = resultSet.getBoolean("sent");
                    entity.type = resultSet.getString("type");
                    NotificationPayload.BookingSuccess payload = objectMapper.readValue(resultSet.getString("payload"), NotificationPayload.BookingSuccess.class);
                    entity.bookingId = payload.getBookingId();
                    entity.itemId = payload.getItemId();
                    entity.start = payload.getStart();
                    entity.end = payload.getEnd();
                    entity.nights = payload.getNights();
                    entity.bookerEmail = payload.getBookerEmail();

                    return entity;
                } catch (JsonProcessingException e) {
                    log.error("Could not parse notification payload from json. Notifiaction Id {}, reason {}", resultSet.getObject("id").toString(), e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        }
    }
}