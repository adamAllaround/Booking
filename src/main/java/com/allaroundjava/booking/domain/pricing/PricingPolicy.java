package com.allaroundjava.booking.domain.pricing;

import com.allaroundjava.booking.common.Interval;
import com.allaroundjava.booking.common.Money;

interface PricingPolicy {
    Money calculatePrice();

    static PricingPolicy flat(Money rate, Interval interval) {
        return new FlatPrice(rate, interval);
    }
}

