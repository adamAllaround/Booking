package com.allaroundjava.booking.bookings.pricing;

import java.util.Arrays;
import java.util.Optional;

public enum PaymentType {
    WIRE_TRANSFER,
    INTERNET_PAYMENT,
    BLIK;

    public static Optional<PaymentType> of(String type) {
        return Arrays.stream(values())
                .filter(val -> val.name().equals(type))
                .findAny();
    }
}
