package com.allaroundjava.booking.bookings.adapters.db;

import com.allaroundjava.booking.bookings.domain.model.*;
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent.AddAvailabilitySuccess;
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent.BookingSuccess;
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent.RemoveAvailabilitySuccess;
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent.RemoveBookingSuccess;
import com.allaroundjava.booking.bookings.domain.ports.OccupationRepository;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RoomOccupationDatabaseRepository implements OccupationRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Clock clock;

    @Override
    public RoomOccupation find(UUID roomId, Interval interval) {
        RoomDetailsDatabaseEntity roomDetails = jdbcTemplate.queryForObject("SELECT * from Rooms where id=:id",
                ImmutableMap.of("id", roomId),
                new RoomDetailsDatabaseEntity.RowMapper());

        Map<String,Object> availParams = ImmutableMap.of("roomId", roomId,
                "startTime", Timestamp.from(interval.getStart()),
                "endTime", Timestamp.from(interval.getEnd()));

        List<Availability> availabilities = jdbcTemplate.query("select * from availabilities where itemId=:roomId and endTime >:startTime and startTime <:endTime",
                availParams,
                new AvailabilityDatabaseEntity.RowMapper())
                .stream()
                .map(AvailabilityDatabaseEntity::toModel)
                .collect(Collectors.toList());

        List<Booking> bookings = jdbcTemplate.query("select * from bookings where itemId=:roomId and endTime >:startTime and startTime <:endTime",
                availParams,
                new BookingDatabaseEntity.RowMapper())
                .stream()
                .map(BookingDatabaseEntity::toModel)
                .collect(Collectors.toList());

        return new RoomOccupation(roomDetails.getId(),
                Availabilities.from(roomDetails.getId(), roomDetails.hotelHourStart, roomDetails.hotelHourEnd, availabilities),
                new Bookings(bookings),
                BookingPolicies.allHotelRoomPolicies(clock));
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
            saveNewBooking((BookingSuccess) event);
        }

        if (event instanceof RemoveBookingSuccess) {
            removeBooking((RemoveBookingSuccess) event);
        }
    }

    private void saveNewAvailabilities(AddAvailabilitySuccess event) {
        List<Availability> availabilities = event.getAvailabilityList();

        jdbcTemplate.batchUpdate("insert into Availabilities (id, itemId, startTime, endTime) values (:id,:itemId, :start, :end);",
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
                "start", Timestamp.from(availability.getStart().atZone(ZoneOffset.UTC).toInstant()),
                "end", Timestamp.from(availability.getEnd().atZone(ZoneOffset.UTC).toInstant())));
    }

    private void removeAvailability(RemoveAvailabilitySuccess event) {
        ImmutableMap<String, Object> params = ImmutableMap.of("id", event.getAvailability().getId());
        jdbcTemplate.update("delete from Availabilities where id=:id", params);
    }

    private void saveNewBooking(BookingSuccess event) {
        insertIntoBooking(event.getBookingId(), event.getItemId(), event.getInterval());
        updateAvailabilities(event.getAvailabilityIds(), event.getBookingId());
    }

    private void insertIntoBooking(UUID bookingId, UUID itemId, Interval interval) {
        ImmutableMap<String, Object> params = ImmutableMap.of("id", bookingId,
                "itemId", itemId,
                "start", Timestamp.from(interval.getStart().atZone(ZoneOffset.UTC).toInstant()),
                "end", Timestamp.from(interval.getEnd().atZone(ZoneOffset.UTC).toInstant()));
        jdbcTemplate.update("insert into Bookings (id, itemId, startTime, endTime) values (:id,:itemId, :start, :end);", params);
    }

    private void updateAvailabilities(Set<UUID> availabilityIds, UUID bookingId) {
        ImmutableMap<String, Object> params = ImmutableMap.of("bookingId", bookingId,
                "availabilityIds", availabilityIds);
        jdbcTemplate.update("UPDATE Availabilities set bookingId=:bookingId where id in (:availabilityIds)", params);
    }

    private void removeBooking(RemoveBookingSuccess event) {
        ImmutableMap<String, Object> params = ImmutableMap.of("id", event.getAvailability().getId());
        jdbcTemplate.update("delete from Bookings where id=:id", params);
    }
}