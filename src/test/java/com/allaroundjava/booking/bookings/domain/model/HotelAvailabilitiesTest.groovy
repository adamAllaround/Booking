package com.allaroundjava.booking.bookings.domain.model

import spock.lang.Specification

import java.time.Instant
import java.time.temporal.ChronoField

import static com.allaroundjava.booking.bookings.domain.model.Dates2020.may
import static com.allaroundjava.booking.bookings.domain.model.HotelAvailabilitiesFixture.*

class HotelAvailabilitiesTest extends Specification {

    def "Can add all availabilities when empty"() {
        Availabilities availabilities = standardEmpty()
        def candidateInterval = new Interval(may(20).hour(10), may(23).hour(10))

        when:
        def result = availabilities.tryAdd(candidateInterval)

        then:
        result.isPresent()
        result.get().size() == 3

        and:
        def resultList = result.get()
        isStandardHotelNightAvailabilityOn(resultList.get(0), 20)
        isStandardHotelNightAvailabilityOn(resultList.get(1), 21)
        isStandardHotelNightAvailabilityOn(resultList.get(2), 22)
    }

    def "Can add availability when beginning of interval taken"() {
        Availabilities availabilities = withExistingInterval(new Interval(may(20).hour(15), may(21).hour(10)))

        when:
        def result = availabilities.tryAdd(new Interval(may(20).hour(10), may(22).hour(10)))

        then:
        result.isPresent()
        result.get().size() == 1

        and:
        def resultList = result.get()
        isStandardHotelNightAvailabilityOn(resultList.get(0), 21)
    }

    def "Can add availability when beginning and end of interval taken"() {
        Availabilities availabilities = withExistingIntervals(
                [new Interval(may(20).hour(15), may(21).hour(10)),
                 new Interval(may(22).hour(15), may(23).hour(10))])

        when:
        def result = availabilities.tryAdd(new Interval(may(20).hour(10), may(24).hour(10)))

        then:
        result.isPresent()
        result.get().size() == 2

        and:
        def resultList = result.get()
        isStandardHotelNightAvailabilityOn(resultList.get(0), 21)
        isStandardHotelNightAvailabilityOn(resultList.get(1), 23)
    }

    def "Can add availabilities around existing availability when interval big enough"() {
        Availabilities availabilities = withExistingInterval(new Interval(may(20).hour(10), may(21).hour(10)))

        when:
        def result = availabilities.tryAdd(new Interval(may(19).hour(13), may(22).hour(10)))

        then:
        result.isPresent()
        result.get().size() == 2

        and:
        def resultList = result.get()
        isStandardHotelNightAvailabilityOn(resultList.get(0), 19)
        isStandardHotelNightAvailabilityOn(resultList.get(1), 21)
    }

    def "Cannot add availability when interval fully covered with existing availabilities"() {
        Availabilities availabilities = withExistingInterval(new Interval(may(20).hour(10), may(21).hour(10)))

        when:
        def result = availabilities.tryAdd(new Interval(may(20).hour(13), may(21).hour(12)))

        then:
        !result.isPresent()
    }

    def "Is Non continuous when break in availabilities"() {
        when:
        Availabilities availabilities = withExistingIntervals(
                [new Interval(may(20).hour(15), may(21).hour(10)),
                 new Interval(may(22).hour(15), may(23).hour(10))])

        then:
        !availabilities.isContinuous()
    }

    def "Is Non continuous when break in unordered availabilities"() {
        when:
        Availabilities availabilities = withExistingIntervals(
                [new Interval(may(22).hour(15), may(23).hour(10)),
                 new Interval(may(20).hour(15), may(21).hour(10)),
                 new Interval(may(23).hour(15), may(24).hour(10)),
                ])

        then:
        !availabilities.isContinuous()
    }

    def "Is continuous when no break in availabilities"() {
        when:
        Availabilities availabilities = withExistingIntervals(
                [new Interval(may(22).hour(15), may(23).hour(10)),
                 new Interval(may(21).hour(15), may(22).hour(10)),
                 new Interval(may(22).hour(15), may(23).hour(10)),
                ])

        then:
        availabilities.isContinuous()
    }

    def "Is non continuous when empty"() {
        when:
        Availabilities availabilities = standardEmpty()

        then:
        !availabilities.isContinuous()
    }

    def "Is continuous when single"() {
        when:
        Availabilities availabilities = withConcreteAvailabilityList([AvailabilityFixture.MAY11])

        then:
        availabilities.isContinuous()
    }

    void isStandardHotelNightAvailabilityOn(Availability availability, int startDay) {
        assert availability.start == Instant.from(may(startDay).hour(STANDARD_HOTEL_START.get(ChronoField.HOUR_OF_DAY)))
        assert availability.end == Instant.from(may(startDay + 1).hour(STANDARD_HOTEL_END.get(ChronoField.HOUR_OF_DAY)))
    }
}
