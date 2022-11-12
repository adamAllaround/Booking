package com.allaroundjava.booking.domain.pricing;

import com.allaroundjava.booking.common.Interval;
import com.allaroundjava.booking.common.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PricingService {
    private final QueryForPrice query;
    private final ReservationPricesRepository reservationPricesRepository;

    public void setPrice(UUID reservationId, UUID roomId, LocalDate dateFrom, LocalDate dateTo, int guests) {
        Interval interval = new Interval(dateFrom.atStartOfDay().toInstant(ZoneOffset.UTC),
                dateTo.atTime(23, 59).toInstant(ZoneOffset.UTC));

        Money money = query.priceFor(roomId, interval);
        reservationPricesRepository.setReservationPrice(reservationId, money);
    }
}
