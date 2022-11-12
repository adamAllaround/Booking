package com.allaroundjava.booking.adapters.db;

import com.allaroundjava.booking.common.Interval;
import com.allaroundjava.booking.domain.readmodel.AvailabilitySearch;
import com.google.common.collect.ImmutableMap;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
class AvailabilityReadModel implements AvailabilitySearch {
    private static final String AVAILABILITY_SEARCH_QUERY = "select id from roommeta where ownerId=:ownerId and id not in (select roomId from reservations where (datefrom >= :searchFrom and datefrom < :searchTo) or (dateto > :searchFrom and dateto <= :searchTo))";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Set<UUID> findAvailableRoomsIn(UUID ownerId, Interval interval) {
        Map<String, Object> params = ImmutableMap.of(
                "ownerId", ownerId,
                "searchFrom", java.sql.Date.valueOf(interval.getStartDate()),
                "searchTo", java.sql.Date.valueOf(interval.getEndDate())
        );

        List<AvailabilitySearchEntity> result = jdbcTemplate.query(AVAILABILITY_SEARCH_QUERY,
                params,
                new BeanPropertyRowMapper<>(AvailabilitySearchEntity.class));

        return result.stream()
                .map(AvailabilitySearchEntity::getId)
                .collect(Collectors.toSet());
    }
}

@Data
class AvailabilitySearchEntity {
    UUID id;
}
