package com.allaroundjava.booking.bookings.domain.command;

import com.allaroundjava.booking.bookings.shared.Interval;
import lombok.Value;

import java.util.UUID;
@Value
public class AddAvailabilityCommand {
    UUID roomId;
    Interval interval;
}
