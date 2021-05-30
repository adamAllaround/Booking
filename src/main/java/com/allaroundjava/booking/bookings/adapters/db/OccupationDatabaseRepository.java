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
        ItemDatabaseEntity item = jdbcTemplate.queryForObject("SELECT i.* from OccupationItems i where id=:id",
                params,
                new BeanPropertyRowMapper<>(ItemDatabaseEntity.class));

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

        return new Occupation(new Item(id), bookings, Availabilities.from(item.getItemType(), item.getHotelHourStart(), item.getHotelHourEnd(), availabilities));
    }

    @Override
    public void handle(OccupationEvent event) {
        if (event instanceof AddAvailabilitySuccess) {
            saveNewAvailability((AddAvailabilitySuccess) event);
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

    private void saveNewAvailability(AddAvailabilitySuccess event) {
        Availability availability = event.getAvailability();
        ImmutableMap<String, Object> params = ImmutableMap.of("id", availability.getId(),
                "itemId", availability.getItemId(),
                "start", OffsetDateTime.ofInstant(availability.getStart(), ZoneOffset.UTC),
                "end", OffsetDateTime.ofInstant(availability.getEnd(), ZoneOffset.UTC));
        jdbcTemplate.update("insert into Availabilities (id, itemId, start, end) values (:id,:itemId, :start, :end);", params);
    }

    private void removeAvailability(RemoveAvailabilitySuccess event) {
        ImmutableMap<String, Object> params = ImmutableMap.of("id", event.getAvailability().getId());
        jdbcTemplate.update("delete from Availabilities a where a.id=:id", params);
    }

    private void saveNewBooking(BookingSuccess event) {
        Booking booking = event.getBooking();
        ImmutableMap<String, Object> params = ImmutableMap.of("id", booking.getId(),
                "itemId", booking.getItemId(),
                "start", OffsetDateTime.ofInstant(booking.getStart(), ZoneOffset.UTC),
                "end", OffsetDateTime.ofInstant(booking.getEnd(), ZoneOffset.UTC));
        jdbcTemplate.update("insert into Bookings (id, itemId, start, end) values (:id,:itemId, :start, :end);", params);
    }

    private void removeBooking(RemoveBookingSuccess event) {
        ImmutableMap<String, Object> params = ImmutableMap.of("id", event.getAvailability().getId());
        jdbcTemplate.update("delete from Bookings b where b.id=:id", params);
    }
}
