package com.allaroundjava.booking.bookings.domain.ports;

import com.allaroundjava.booking.bookings.shared.Money;

import java.util.UUID;

public interface ReservationPricesRepository {
    void setReservationPrice(UUID reservationId, Money money);
}
