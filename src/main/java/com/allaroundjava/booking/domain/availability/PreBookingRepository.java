package com.allaroundjava.booking.domain.availability;

import com.google.common.collect.ImmutableMap;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
class PreBookingRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    void preBook(UUID reservationId, UUID roomId, LocalDate dateFrom, LocalDate dateTo) {
        Map<String, Object> countQueryParams = ImmutableMap.of("reservationId", reservationId);

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM prebookings where reservationId=:reservationId", countQueryParams, Integer.class);

        if(count > 0) {
            updateExistingPreBookingRecord(reservationId, roomId, dateFrom, dateTo);
        } else {
            createNewPreBooking(reservationId, roomId, dateFrom, dateTo);
        }
    }

    private void updateExistingPreBookingRecord(UUID reservationId, UUID roomId, LocalDate dateFrom, LocalDate dateTo) {
        Map<String, Object> params = constructQueryParams(reservationId, roomId, dateFrom, dateTo);

        jdbcTemplate.update("UPDATE prebookings set roomId=:roomId, dateFrom=:dateFrom, dateTo=:dateTo where reservationId=:reservationId", params);
    }

    private void createNewPreBooking(UUID reservationId, UUID roomId, LocalDate dateFrom, LocalDate dateTo) {
        Map<String, Object> params = constructQueryParams(reservationId, roomId, dateFrom, dateTo);

        jdbcTemplate.update("INSERT INTO prebookings (reservationId, roomId, dateFrom, dateTo) values (:reservationId, :roomId, :dateFrom, :dateTo)", params);
    }

    private Map<String, Object> constructQueryParams(UUID reservationId, UUID roomId, LocalDate dateFrom, LocalDate dateTo) {
        return ImmutableMap.<String, Object>builder()
                .put("reservationId", reservationId)
                .put("roomId", roomId)
                .put("dateFrom", java.sql.Date.valueOf(dateFrom))
                .put("dateTo", java.sql.Date.valueOf(dateTo))
                .build();
    }

    public PreBookingDto getByReservationId(UUID reservationId) {
        Map<String, Object> param = ImmutableMap.of("reservationId", reservationId);

        PreBookingEntity entity = jdbcTemplate.queryForObject("SELECT * FROM prebookings where reservationId=:reservationId",
                param,
                new BeanPropertyRowMapper<>(PreBookingEntity.class));

        return new PreBookingDto(entity.getReservationId(), entity.getRoomId(), entity.getDateFrom().toLocalDate(), entity.getDateTo().toLocalDate());
    }
}

@Data
class PreBookingEntity {
    private UUID reservationId;
    private UUID roomId;
    private Date dateFrom;
    private Date dateTo;
}
