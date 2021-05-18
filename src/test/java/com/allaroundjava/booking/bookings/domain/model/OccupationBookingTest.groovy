package com.allaroundjava.booking.bookings.domain.model


import spock.lang.Specification

import static com.allaroundjava.booking.bookings.domain.model.CommandResult.failure
import static com.allaroundjava.booking.bookings.domain.model.CommandResult.success
import static com.allaroundjava.booking.bookings.domain.model.Dates2020.march
import static com.allaroundjava.booking.bookings.domain.model.OccupationFixture.withAvailabilityBetween
import static com.allaroundjava.booking.bookings.domain.model.OccupationFixture.withBookingBetween

class OccupationBookingTest extends Specification {
    private static final UUID ITEM_ID = UUID.randomUUID()

    def "Can add booking to cover existing availability"() {
        given:
        Occupation occupation = withAvailabilityBetween(march(10).hour(15), march(11).hour(15))

        when:
        def result = occupation.addBooking(new Interval(march(10).hour(15), march(11).hour(15)))

        then:
        success(result)
    }

    def "Can add booking to cover several availabilities"() {

    }

    def "Cannot add booking when availability not covered fully" () {
        given:
        Occupation occupation = withAvailabilityBetween(march(10).hour(15), march(11).hour(15))

        when:
        def result = occupation.addBooking(new Interval(march(11).hour(12), march(11).hour(15)))

        then:
        failure(result)
    }

    def "Can add booking side by side"() {
        given:
        Occupation occupation = withBookingBetween(march(10).hour(15), march(11).hour(15))
        occupation.addAvailability(ITEM_ID, new Interval(march(11).hour(16), march(12).hour(15)))

        when:
        def result = occupation.addBooking(new Interval(march(11).hour(16), march(12).hour(15)))

        then:
        success(result)
    }

    def "Cannot add booking in the past" () {

    }

    def "Cannot add booking twice"() {
        given:
        Occupation occupation = withBookingBetween(march(10).hour(15), march(11).hour(15))

        when:
        def result = occupation.addBooking(new Interval(march(10).hour(15), march(11).hour(15)))

        then:
        failure(result)
    }

    def "Can remove existing booking"() {
        given:
        Occupation occupation = withAvailabilityBetween(march(10).hour(15), march(11).hour(15))
        def bookingResult = occupation.addBooking(new Interval(march(10).hour(15), march(11).hour(15)))

        when:
        def result = occupation.removeBooking(bookingResult.get().booking)

        then:
        success(result)
    }

    def "Can remove booking and add new one in same slot"() {
        given:
        Occupation occupation = withAvailabilityBetween(march(10).hour(15), march(11).hour(15))
        def bookingResult = occupation.addBooking(new Interval(march(10).hour(15), march(11).hour(15)))
        and:
        occupation.removeBooking(bookingResult.get().booking)

        when:
        def result = occupation.addBooking(new Interval(march(10).hour(15), march(11).hour(15)))

        then:
        success(result)
    }
}
