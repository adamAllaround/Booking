package com.allaroundjava.booking.notifications;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class Interval {
    private final Instant start;
    private final Instant end;
}
