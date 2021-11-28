package com.allaroundjava.booking.bookings.domain.command;

import com.allaroundjava.booking.bookings.domain.model.Interval;
import lombok.Value;

import java.util.UUID;

@Value
public class AddBasketCommand {
    UUID roomId;
    Interval interval;
}
