package com.allaroundjava.booking.bookings.domain.model


import spock.lang.Specification

import static com.allaroundjava.booking.bookings.domain.model.CommandResult.failure
import static com.allaroundjava.booking.bookings.domain.model.CommandResult.success
import static com.allaroundjava.booking.bookings.domain.model.Dates2020.march
import static com.allaroundjava.booking.bookings.domain.model.OccupationFixture.emptyOccupation
import static com.allaroundjava.booking.bookings.domain.model.OccupationFixture.withAvailabilityBetween

class OccupationAvailabilityTest extends Specification {
    private static UUID ITEM_ID = UUID.randomUUID()

    def "Can add availability to empty occupation"() {
        given:
        Occupation occupation = emptyOccupation()

        when:
        def result = occupation.addAvailability(ITEM_ID, new Interval(march(13).hour(15), march(14).hour(10)))

        then:
        success(result)
    }

    def "Cannot add overlapping availability"() {
        given:
        Occupation occupation = withAvailabilityBetween(march(13).hour(16), march(14).hour(16))

        when:
        def result = occupation.addAvailability(ITEM_ID, new Interval(march(13).hour(12), march(14).hour(12)))

        then:
        failure(result)
    }

    def "Can remove existing availability"() {
        given:
        Occupation occupation = emptyOccupation()

        and:
        def addResult = occupation.addAvailability(ITEM_ID, new Interval(march(10).hour(12), march(11).hour(13)))

        when:
        def result = occupation.removeAvailability(addResult.map({ success -> success.availabilityList }).get().get(0))

        then:
        success(result)
    }

    def "Cannot remove non-existent availability"() {
        given:
        Occupation occupation = withAvailabilityBetween(march(10).hour(12), march(14).hour(12))

        when:
        def result = occupation.removeAvailability(Availability.from(ITEM_ID, new Interval(march(13).hour(7), march(17).hour(7))))

        then:
        failure(result)
    }

    def "Can Remove availability add add it afterwards in same slot"() {
        given:
        Occupation occupation = emptyOccupation()

        and:
        occupation.addAvailability(ITEM_ID, new Interval(march(10).hour(12), march(13).hour(13))).get().availabilityList
        def toRemove = occupation.addAvailability(ITEM_ID, new Interval(march(13).hour(14), march(14).hour(15))).get().availabilityList

        when:
        def result = occupation.removeAvailability(toRemove.get(0))

        then:
        success(result)

        and:
        success(occupation.addAvailability(ITEM_ID, new Interval(march(13).hour(15), march(15).hour(15))))
    }
}
