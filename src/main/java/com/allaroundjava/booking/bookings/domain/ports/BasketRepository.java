package com.allaroundjava.booking.bookings.domain.ports;

import com.allaroundjava.booking.bookings.domain.model.Basket;
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent;

import java.util.Optional;
import java.util.UUID;

public interface BasketRepository {
    void handle(OccupationEvent.BasketAddSuccess basketAddSuccess);

    Optional<Basket> getSingle(UUID basketId);
}
