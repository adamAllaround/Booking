package com.allaroundjava.booking.bookings.availability;

import com.google.common.collect.ImmutableMap;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
class JpaSlotRepository implements SlotRepository {
    private static final String RESERVATION_SEARCH_QUERY = "select reservationId from reservations where (datefrom >= :searchFrom and datefrom < :searchTo) or (dateto > :searchFrom and dateto <= :searchTo)";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public ItemTimeSlot slotBetween(UUID roomId, LocalDate dateFrom, LocalDate dateTo) {
        Map<String, Object> params = ImmutableMap.of(
                "roomId", roomId,
                "searchFrom", java.sql.Date.valueOf(dateFrom),
                "searchTo", java.sql.Date.valueOf(dateTo)
        );

        List<ReservationEntity> result = jdbcTemplate.query(RESERVATION_SEARCH_QUERY,
                params,
                new BeanPropertyRowMapper<>(ReservationEntity.class));

        Set<UUID> reservationIds = result.stream()
                .map(ReservationEntity::getId)
                .collect(Collectors.toSet());

        return new ItemTimeSlot(roomId, reservationIds, dateFrom, dateTo);
    }
}

@Data
class ReservationEntity {
    UUID id;
}
