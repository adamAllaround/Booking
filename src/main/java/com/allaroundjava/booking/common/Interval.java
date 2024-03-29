package com.allaroundjava.booking.common;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class Interval extends ValueObject {
    private final Instant start;
    private final Instant end;

    public Interval(@NonNull Instant start, @NonNull Instant end) {
        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("Invalid interval. Start Date must be before End Date");
        }
        this.start = start;
        this.end = end;
    }

    public boolean overlaps(Interval interval) {
        return !(end.isBefore(interval.start) || start.isAfter(interval.end));
    }

    public Interval plusDays(int days) {
        Instant newStart = start.plus(days, ChronoUnit.DAYS);
        Instant newEnd = end.plus(days, ChronoUnit.DAYS);
        return new Interval(newStart, newEnd);
    }

    public Interval expand(Interval interval) {
        if (start.isBefore(interval.start)) {
            return new Interval(start, interval.end);
        } else {
            return new Interval(interval.start, end);
        }
    }

    public List<Interval> multiplyTill(Instant end) {
        List<Interval> result = new ArrayList<>();
        Interval seek = this;

        while (!seek.getEnd().isAfter(end)) {
            result.add(new Interval(seek.start, seek.end));
            seek = seek.plusDays(1);
        }
        return result;
    }

    public long getDays() {
        return Duration.between(start, end).toDays();
    }

    public LocalDate getStartDate() {
        return LocalDate.ofInstant(start, ZoneOffset.UTC);
    }

    public LocalDate getEndDate() {
        return LocalDate.ofInstant(end, ZoneOffset.UTC);
    }
}
