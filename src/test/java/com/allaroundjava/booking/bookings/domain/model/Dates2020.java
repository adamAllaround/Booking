package com.allaroundjava.booking.bookings.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.time.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Dates2020 {
    LocalDate date;
    LocalTime time;


    Instant hour(int hour) {
        return LocalDateTime.of(this.date, LocalTime.of(hour, 0)).toInstant(ZoneOffset.UTC);
    }

    static Dates2020 january(int day) {
        return new Dates2020(LocalDate.of(2020, 1, day), LocalTime.of(0, 0));
    }

    static Dates2020 february(int day) {
        return new Dates2020(LocalDate.of(2020, 2, day), LocalTime.of(0, 0));
    }

    static Dates2020 march(int day) {
        return new Dates2020(LocalDate.of(2020, 3, day), LocalTime.of(0, 0));
    }

    static Dates2020 april(int day) {
        return new Dates2020(LocalDate.of(2020, 4, day), LocalTime.of(0, 0));
    }

    static Dates2020 may(int day) {
        return new Dates2020(LocalDate.of(2020, 5, day), LocalTime.of(0, 0));
    }

    static Dates2020 june(int day) {
        return new Dates2020(LocalDate.of(2020, 6, day), LocalTime.of(0, 0));
    }
}
