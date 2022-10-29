package com.allaroundjava.booking.bookings.pricing;

import com.allaroundjava.booking.bookings.shared.Interval;
import com.allaroundjava.booking.bookings.shared.Money;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
class FlatPrice implements PricingPolicy {
    private final Money rate;
    private final Interval interval;

    @Override
    public Money calculatePrice() {
        return rate.multiplyBy(BigDecimal.valueOf(interval.getDays()));
    }
}