package com.allaroundjava.booking.bookings.adapters.db;

import com.allaroundjava.booking.bookings.domain.model.*;
import com.allaroundjava.booking.bookings.domain.ports.OccupationRepository;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
public class OccupationDatabaseRepository implements OccupationRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Occupation findById(UUID id) {
        ImmutableMap<String, UUID> params = ImmutableMap.of("id", id);
        List<Availability> availabilities = jdbcTemplate.query("select a.* from Availabilities a where a.itemId=:id",
                params,
                new BeanPropertyRowMapper<>(AvailabilityDatabaseEntity.class))
                .stream()
                .map(AvailabilityDatabaseEntity::toModel)
                .collect(Collectors.toList());

        List<Booking> bookings = jdbcTemplate.query("select b.* from Bookings b where itemId=:id", params,
                new BeanPropertyRowMapper<>(BookingDatabaseEntity.class))
                .stream()
                .map(BookingDatabaseEntity::toModel)
                .collect(Collectors.toList());

        return new Occupation(new Item(id), bookings, new Availabilities(availabilities));
    }

    @Override
    public void handle(OccupationEvent event) {
        if (event instanceof OccupationEvent.AddAvailabilitySuccess) {
            saveNewAvailability((OccupationEvent.AddAvailabilitySuccess) event);
        }
        if (event instanceof OccupationEvent.RemoveAvailabilitySuccess) {
            removeAvailability((OccupationEvent.RemoveAvailabilitySuccess) event);
        }
        if (event instanceof OccupationEvent.BookingSuccess) {

        }
    }

    private void saveNewAvailability(OccupationEvent.AddAvailabilitySuccess event) {
        Availability availability = event.getAvailability();
        ImmutableMap<String, Object> params = ImmutableMap.of("id", availability.getId(),
                "itemId", availability.getItemId(),
                "start", OffsetDateTime.ofInstant(availability.getStart(), ZoneOffset.UTC),
                "end", OffsetDateTime.ofInstant(availability.getEnd(), ZoneOffset.UTC));
        jdbcTemplate.update("insert into Availabilities (id, itemId, start, end) values (:id,:itemId, :start, :end);", params);
    }

    private void removeAvailability(OccupationEvent.RemoveAvailabilitySuccess event) {
        Availability availability = event.getAvailability();
        ImmutableMap<String, Object> params = ImmutableMap.of("id", availability.getId());
        jdbcTemplate.update("delete from Availabilities where id=:id", params);
    }
}
