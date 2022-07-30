package com.allaroundjava.booking.bookings.readmodel;

import com.allaroundjava.booking.bookings.shared.Money;
import lombok.Value;

import java.time.OffsetTime;
import java.util.UUID;


@Value
public class RoomDetail {
    UUID roomId;
    String name;
    String description;
    int capacity;
    OffsetTime arrivalHour;
    OffsetTime departureHour;
    Money price;

    public RoomDetail withPrice(Money price) {
        return new RoomDetail(roomId, name, description, capacity, arrivalHour, departureHour, price);
    }
}
