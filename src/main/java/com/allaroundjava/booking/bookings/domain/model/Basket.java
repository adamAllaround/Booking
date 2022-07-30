package com.allaroundjava.booking.bookings.domain.model;

import com.allaroundjava.booking.bookings.shared.Interval;
import com.allaroundjava.booking.common.Entity;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Basket extends Entity {
    private final UUID roomId;
    private final Interval interval;

    public Basket(UUID id, UUID roomId, Interval interval) {
        super(id);
        this.roomId = roomId;
        this.interval = interval;
    }

    public static Basket createNew(UUID roomId, Interval interval) {
        return new Basket(UUID.randomUUID(), roomId, interval);
    }
}
