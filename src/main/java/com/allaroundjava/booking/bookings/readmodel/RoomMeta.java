package com.allaroundjava.booking.bookings.readmodel;

import java.util.Set;
import java.util.UUID;

public interface RoomMeta {
    Set<RoomDetail> find(Set<UUID> availableRoomIds, Integer capacity);
}
