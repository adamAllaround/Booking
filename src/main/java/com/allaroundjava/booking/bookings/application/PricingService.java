package com.allaroundjava.booking.bookings.application;

import com.allaroundjava.booking.bookings.domain.ports.ReservationPricesRepository;
import com.allaroundjava.booking.bookings.shared.Interval;
import com.allaroundjava.booking.bookings.shared.Money;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

@RequiredArgsConstructor
public class PricingService {
    private final QueryForPrice query;
    private final ReservationPricesRepository reservationPricesRepository;

    void setPrice(UUID reservationId, UUID roomId, LocalDate dateFrom, LocalDate dateTo, int guests) {
        Interval interval = new Interval(dateFrom.atStartOfDay().toInstant(ZoneOffset.UTC),
                dateTo.atTime(23, 59).toInstant(ZoneOffset.UTC));

        Money money = query.priceFor(roomId, interval);
        reservationPricesRepository.setReservationPrice(reservationId, money);
    }
}
