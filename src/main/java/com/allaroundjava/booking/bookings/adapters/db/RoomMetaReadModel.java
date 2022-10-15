package com.allaroundjava.booking.bookings.adapters.db;

import com.allaroundjava.booking.bookings.readmodel.RoomDetail;
import com.allaroundjava.booking.bookings.readmodel.RoomMeta;
import com.google.common.collect.ImmutableMap;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RoomMetaReadModel implements RoomMeta {
    private static final String SELECT_ROOMMETA_QUERY = "select * from roommeta where capacity >=:capacity and id in (:availableRoomIds)";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Set<RoomDetail> find(Set<UUID> availableRoomIds, Integer capacity) {
        Map<String, Object> params = ImmutableMap.of(
                "availableRoomIds", availableRoomIds,
                "capacity", capacity
                );

        List<RoomMetaEntity> roomMeta = jdbcTemplate.query(SELECT_ROOMMETA_QUERY,
                params,
                new BeanPropertyRowMapper<>(RoomMetaEntity.class));

        return toRoomDetail(roomMeta); //place a search query in room availabilities tables and room details
    }

    private Set<RoomDetail> toRoomDetail(List<RoomMetaEntity> roomMeta) {
        return roomMeta.stream()
                .map(this::toDetail)
                .collect(Collectors.toSet());
    }

    private RoomDetail toDetail(RoomMetaEntity entity) {
        return new RoomDetail(entity.id,
                entity.name,
                entity.description,
                entity.capacity,
                entity.arrivalHour.toLocalTime().atOffset(ZoneOffset.UTC),
                entity.departureHour.toLocalTime().atOffset(ZoneOffset.UTC));
    }
}

@Data
class RoomMetaEntity {
    UUID id;
    UUID ownerId;
    String name;
    String description;
    Integer capacity;
    String location;
    Time arrivalHour;
    Time departureHour;
}
