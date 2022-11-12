package com.allaroundjava.booking.domain.pricing;

import com.allaroundjava.booking.common.Money;

import java.util.UUID;

interface ReservationPricesRepository {
    void setReservationPrice(UUID reservationId, Money money);
}
