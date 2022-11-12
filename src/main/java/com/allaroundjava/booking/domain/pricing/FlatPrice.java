package com.allaroundjava.booking.domain.pricing;

import com.allaroundjava.booking.common.Interval;
import com.allaroundjava.booking.common.Money;
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