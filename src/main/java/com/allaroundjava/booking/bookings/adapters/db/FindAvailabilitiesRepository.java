package com.allaroundjava.booking.bookings.adapters.db;

import com.google.common.collect.ImmutableMap;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FindAvailabilitiesRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<Avail> find(UUID ownerId, LocalDate dateFrom, LocalDate dateTo) {
        ImmutableMap<String, Object> params = ImmutableMap.of("ownerId", ownerId,
                "dateFrom", Timestamp.from(dateFrom.atStartOfDay(ZoneOffset.UTC).toInstant()),
                "dateTo", Timestamp.from(dateTo.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC)),
                "numDays", Period.between(dateFrom, dateTo).getDays());
        return jdbcTemplate.query("select * from Rooms where ownerId=:ownerId and id in " +
                        "(select itemId from (select itemId, count(*) from availabilities where bookingId is null and startTime >= :dateFrom and endTime <= :dateTo group by itemId having count(itemId) = :numDays) as avail)",
                params,
                new Avail.RowMapper());
    }


    @Data
    public static class Avail {
        UUID roomId;
        int capacity;
        String name;
        OffsetTime hotelHourStart;
        OffsetTime hotelHourEnd;

        static class RowMapper implements org.springframework.jdbc.core.RowMapper<Avail> {

            @Override
            public Avail mapRow(ResultSet resultSet, int i) throws SQLException {
                Avail entity = new Avail();
                entity.roomId = UUID.fromString(resultSet.getObject("id").toString());
                entity.capacity = resultSet.getInt("capacity");
                entity.name = resultSet.getString("name");
                entity.hotelHourStart = OffsetTime.of(resultSet.getTime("hotelHourStart").toLocalTime(), ZoneOffset.UTC);
                entity.hotelHourEnd = OffsetTime.of(resultSet.getTime("hotelHourEnd").toLocalTime(), ZoneOffset.UTC);

                return entity;
            }
        }
    }
}
