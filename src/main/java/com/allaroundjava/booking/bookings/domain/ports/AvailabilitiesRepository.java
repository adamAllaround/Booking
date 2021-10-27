package com.allaroundjava.booking.bookings.domain.ports;

import com.allaroundjava.booking.bookings.domain.model.Availability;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AvailabilitiesRepository {
//    List<Availability> getAllByItemId(UUID itemId);

    Optional<Availability> getSingle(UUID uuid); //TODO - only used in tests - can remove probably

    List<Availability> getAllByIds(Collection<UUID> ids); //TODO - only used in tests - can remove probably
}
