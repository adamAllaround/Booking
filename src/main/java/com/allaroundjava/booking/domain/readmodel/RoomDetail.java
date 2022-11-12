package com.allaroundjava.booking.domain.readmodel;

import com.allaroundjava.booking.common.Money;
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

    private RoomDetail(UUID roomId, String name, String description, int capacity, OffsetTime arrivalHour, OffsetTime departureHour, Money price) {
        this.roomId = roomId;
        this.name = name;
        this.description = description;
        this.capacity = capacity;
        this.arrivalHour = arrivalHour;
        this.departureHour = departureHour;
        this.price = price;
    }

    public RoomDetail(UUID roomId, String name, String description, int capacity, OffsetTime arrivalHour, OffsetTime departureHour) {
        this(roomId, name, description, capacity, arrivalHour, departureHour, null);
    }

    public RoomDetail withPrice(Money price) {
        return new RoomDetail(roomId, name, description, capacity, arrivalHour, departureHour, price);
    }
}
