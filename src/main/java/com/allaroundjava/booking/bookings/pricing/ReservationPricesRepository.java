package com.allaroundjava.booking.bookings.pricing;

import com.allaroundjava.booking.bookings.shared.Money;

import java.util.UUID;

interface ReservationPricesRepository {
    void setReservationPrice(UUID reservationId, Money money);
}
