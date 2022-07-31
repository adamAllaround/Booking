package com.allaroundjava.booking.bookings.domain.model.pricing;

import com.allaroundjava.booking.bookings.shared.Money;

public interface PricingPolicy {
    Money calculatePrice();
}

