package com.allaroundjava.booking;

import com.allaroundjava.booking.bookings.adapters.db.RoomsDatabaseRepository;
import com.allaroundjava.booking.bookings.domain.model.Dates2020;
import lombok.AllArgsConstructor;

import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.UUID;

@AllArgsConstructor
public class RoomFixtures {

    private final RoomsDatabaseRepository roomsDatabaseRepository;

    public void existsRoom(UUID roomId, UUID ownerId) {

        RoomsDatabaseRepository.RoomDatabaseEntity room = new RoomsDatabaseRepository.RoomDatabaseEntity(roomId,
                ownerId,
                "test name",
                3,
                "test location",
                OffsetTime.of(15, 0, 0, 0, ZoneOffset.UTC),
                OffsetTime.of(10, 0, 0, 0, ZoneOffset.UTC),
                Dates2020.may(20).hour(12));
        roomsDatabaseRepository.save(room);
    }
}
