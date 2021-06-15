package com.allaroundjava.booking.bookings.adapters.db;

import com.allaroundjava.booking.bookings.domain.model.*;
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent.AddAvailabilitySuccess;
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent.BookingSuccess;
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent.RemoveAvailabilitySuccess;
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent.RemoveBookingSuccess;
import com.allaroundjava.booking.bookings.domain.ports.OccupationRepository;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class OccupationDatabaseRepository implements OccupationRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Occupation findById(UUID id) {
        ImmutableMap<String, UUID> params = ImmutableMap.of("id", id);
        Item item = jdbcTemplate.queryForObject("SELECT i.* from OccupationItems i where id=:id",
                params,
                new BeanPropertyRowMapper<>(ItemDatabaseEntity.class))
                .toModel();

        List<Availability> availabilities = jdbcTemplate.query("select a.* from Availabilities a where a.itemId=:id",
                params,
                new BeanPropertyRowMapper<>(AvailabilityDatabaseEntity.class))
                .stream()
                .map(AvailabilityDatabaseEntity::toModel)
                .collect(Collectors.toList());

        List<Booking> bookings = jdbcTemplate.query("select b.* from Bookings b where b.itemId=:id", params,
                new BeanPropertyRowMapper<>(BookingDatabaseEntity.class))
                .stream()
                .map(BookingDatabaseEntity::toModel)
                .collect(Collectors.toList());

        return new Occupation(id,
                bookings,
                Availabilities.from(item, availabilities),
                BookingPolicies.allHotelRoomPolicies());
    }

    @Override
    public void handle(OccupationEvent event) {
        if (event instanceof AddAvailabilitySuccess) {
            saveNewAvailabilities((AddAvailabilitySuccess) event);
        }
        if (event instanceof RemoveAvailabilitySuccess) {
            removeAvailability((RemoveAvailabilitySuccess) event);
        }
        if (event instanceof BookingSuccess) {
            saveNewBooking((BookingSuccess)event);
        }

        if (event instanceof RemoveBookingSuccess) {
            removeBooking((RemoveBookingSuccess) event);
        }
    }

    private void saveNewAvailabilities(AddAvailabilitySuccess event) {
        List<Availability> availabilities = event.getAvailabilityList();

        jdbcTemplate.batchUpdate("insert into Availabilities (id, itemId, start, end) values (:id,:itemId, :start, :end);",
                batchInsertParams(availabilities));
    }

    private MapSqlParameterSource[] batchInsertParams(List<Availability> availabilities) {
        return availabilities.stream()
                .map(this::toInsertParams)
                .toArray(MapSqlParameterSource[]::new);
    }

    private MapSqlParameterSource toInsertParams(Availability availability) {
        return new MapSqlParameterSource(ImmutableMap.of("id", availability.getId(),
                "itemId", availability.getItemId(),
                "start", OffsetDateTime.ofInstant(availability.getStart(), ZoneOffset.UTC),
                "end", OffsetDateTime.ofInstant(availability.getEnd(), ZoneOffset.UTC)));
    }

    private void removeAvailability(RemoveAvailabilitySuccess event) {
        ImmutableMap<String, Object> params = ImmutableMap.of("id", event.getAvailability().getId());
        jdbcTemplate.update("delete from Availabilities a where a.id=:id", params);
    }

    private void saveNewBooking(BookingSuccess event) {
        insertIntoBooking(event.getBooking());
        updateAvailabilities(event.getBooking());
    }

    private void insertIntoBooking(Booking booking) {
        ImmutableMap<String, Object> params = ImmutableMap.of("id", booking.getId(),
                "itemId", booking.getItemId(),
                "start", OffsetDateTime.ofInstant(booking.getStart(), ZoneOffset.UTC),
                "end", OffsetDateTime.ofInstant(booking.getEnd(), ZoneOffset.UTC));
        jdbcTemplate.update("insert into Bookings (id, itemId, start, end) values (:id,:itemId, :start, :end);", params);
    }

    private void updateAvailabilities(Booking booking) {
        ImmutableMap<String, Object> params = ImmutableMap.of("bookingId", booking.getId(),
                "availabilityIds", booking.getAvailabilityIds());
        jdbcTemplate.update("UPDATE Availabilities a set a.bookingId=:bookingId where a.id in (:availabilityIds)", params);
    }

    private void removeBooking(RemoveBookingSuccess event) {
        ImmutableMap<String, Object> params = ImmutableMap.of("id", event.getAvailability().getId());
        jdbcTemplate.update("delete from Bookings b where b.id=:id", params);
    }
}