package com.allaroundjava.booking.bookings.domain.pricing;

import com.allaroundjava.booking.bookings.shared.Interval;
import com.allaroundjava.booking.bookings.shared.Money;

public interface PricingPolicy {
    Money calculatePrice();

    static PricingPolicy flat(Money rate, Interval interval) {
        return new FlatPrice(rate, interval);
    }
}

