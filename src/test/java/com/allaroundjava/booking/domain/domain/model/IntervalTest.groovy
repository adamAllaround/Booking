package com.allaroundjava.booking.domain.domain.model

import com.allaroundjava.booking.common.Interval
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.ZoneOffset

class IntervalTest extends Specification {

    def "When end before start then exception"() {
        when:
        new Interval(start, end)

        then:
        thrown(IllegalArgumentException)

        where:
        start                                                             | end
        LocalDateTime.of(2020, 5, 10, 10, 0, 0).toInstant(ZoneOffset.UTC) | LocalDateTime.of(2020, 5, 10, 10, 0, 0).toInstant(ZoneOffset.UTC)
        LocalDateTime.of(2020, 5, 10, 10, 0, 0).toInstant(ZoneOffset.UTC) | LocalDateTime.of(2020, 5, 9, 10, 0, 0).toInstant(ZoneOffset.UTC)
        LocalDateTime.of(2020, 5, 10, 10, 0, 0).toInstant(ZoneOffset.UTC) | LocalDateTime.of(2020, 5, 10, 9, 59, 59).toInstant(ZoneOffset.UTC)
    }

    def "Interval is multiplied"() {
        given:
        Interval source = new Interval(
                LocalDateTime.of(2020, 10, 1, 10, 0, 0).toInstant(ZoneOffset.UTC),
                LocalDateTime.of(2020, 10, 2, 11, 0, 0).toInstant(ZoneOffset.UTC))

        when:
        List<Interval> result = source.multiplyTill(
                LocalDateTime.of(2020, 10, 4, 15, 0, 0).toInstant(ZoneOffset.UTC))

        then:
        result.size() == 3
    }

    def "Interval is not multiplied when end date too near"() {
        given:
        Interval source = new Interval(
                LocalDateTime.of(2020, 10, 1, 10, 0, 0).toInstant(ZoneOffset.UTC),
                LocalDateTime.of(2020, 10, 2, 11, 0, 0).toInstant(ZoneOffset.UTC))

        when:
        List<Interval> result = source.multiplyTill(
                LocalDateTime.of(2020, 10, 2, 15, 0, 0).toInstant(ZoneOffset.UTC))

        then:
        result.size() == 1
    }

    def "Result is empty when endDate in the past"() {
        given:
        Interval source = new Interval(
                LocalDateTime.of(2020, 10, 1, 10, 0, 0).toInstant(ZoneOffset.UTC),
                LocalDateTime.of(2020, 10, 2, 11, 0, 0).toInstant(ZoneOffset.UTC))

        when:
        List<Interval> result = source.multiplyTill(
                LocalDateTime.of(2020, 10, 1, 15, 0, 0).toInstant(ZoneOffset.UTC))

        then:
        result.isEmpty()
    }

    def "Interval days calculation"() {
        when:
        Interval source = new Interval(
                LocalDateTime.of(2020, 10, 1, hourStart, 0, 0).toInstant(ZoneOffset.UTC),
                LocalDateTime.of(2020, 10, dayEnd, hourEnd, 0, 0).toInstant(ZoneOffset.UTC))

        then:
        source.getDays() == resultDays

        where:
        hourStart | dayEnd | hourEnd || resultDays
        0         | 1      | 10      || 0
        0         | 2      | 10      || 1
        0         | 10     | 23     || 9
    }
}
