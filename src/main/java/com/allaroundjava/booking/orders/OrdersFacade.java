package com.allaroundjava.booking.orders;

import java.util.UUID;

public class OrdersFacade {
    UUID addNew(UUID roomId, NewOrderMeta meta) {
        return UUID.randomUUID();
    }

    void confirm(UUID orderId) {

    }

    void fulfillPayment(UUID orderId) {

    }
}
