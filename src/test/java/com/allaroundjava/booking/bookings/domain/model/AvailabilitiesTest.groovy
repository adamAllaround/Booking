package com.allaroundjava.booking.bookings.domain.model

import spock.lang.Specification

import static com.allaroundjava.booking.bookings.domain.model.Dates2020.may
import static AvailabilitiesFixture.*

class AvailabilitiesTest extends Specification {

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
}
