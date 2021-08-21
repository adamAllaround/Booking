package com.allaroundjava.booking.notifications;

import com.allaroundjava.booking.notifications.items.HotelRoom;
import com.allaroundjava.booking.notifications.items.ItemsRepository;
import com.allaroundjava.booking.notifications.owners.Owner;
import com.allaroundjava.booking.notifications.owners.OwnersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class NotificationRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    List<Notification> allUnsent() {

        List<Notification> notifications = jdbcTemplate.query("select * from NotificationEvents where sent = false",
                new EventRepository.EventRowMapper(objectMapper))
                .stream()
                .map(EventRepository.EventEntity::toNotification)
                .collect(Collectors.toList());


        //TODO smaller subset required
        Map<UUID, HotelRoom> items = jdbcTemplate.query("SELECT * FROM NotificationsItems",
                new ItemsRepository.ItemDatabaseEntity.RowMapper())
                .stream()
                .collect(Collectors.toMap(ItemsRepository.ItemDatabaseEntity::getId, ItemsRepository.ItemDatabaseEntity::toDomain));

        Map<UUID, Owner> owners = jdbcTemplate.query("SELECT * FROM NotificationsOwners",
                new BeanPropertyRowMapper<>(OwnersRepository.OwnerDatabaseEntity.class))
                .stream()
                .collect(Collectors.toMap(OwnersRepository.OwnerDatabaseEntity::getId, OwnersRepository.OwnerDatabaseEntity::toDomain));

        return notifications.stream()
                .map(notification -> notification.enrichItemsData(items))
                .map(notification -> notification.enrichOwnersData(owners))
                .collect(Collectors.toList());
    }

    void markPublished(Collection<Notification> toPublish) {
        ImmutableMap<String, Object> params = ImmutableMap.of("ids", toPublish.stream()
                .map(Notification::getId)
                .collect(Collectors.toList()));
        jdbcTemplate.update("UPDATE NotificationEvents set sent=true where id in (:ids)", params);
    }
}
