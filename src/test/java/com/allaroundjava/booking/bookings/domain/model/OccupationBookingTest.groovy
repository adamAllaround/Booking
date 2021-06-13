package com.allaroundjava.booking.bookings.domain.model

import spock.lang.Specification

import java.util.stream.Collectors

import static com.allaroundjava.booking.bookings.domain.model.AvailabilityFixture.*
import static com.allaroundjava.booking.bookings.domain.model.AvailabilityFixture.MAY10
import static com.allaroundjava.booking.bookings.domain.model.AvailabilityFixture.MAY12
import static com.allaroundjava.booking.bookings.domain.model.CommandResult.failure
import static com.allaroundjava.booking.bookings.domain.model.CommandResult.success
import static com.allaroundjava.booking.bookings.domain.model.HotelAvailabilitiesFixture.withConcreteAvailabilityList
import static com.allaroundjava.booking.bookings.domain.model.OccupationFixture.withConcreteAvailabilities

class OccupationBookingTest extends Specification {
    private static final UUID ITEM_ID = UUID.randomUUID()

    def "Can add booking to cover existing availability"() {
        Availabilities may10Availabilities = withConcreteAvailabilityList([MAY10])
        Occupation occupation = withConcreteAvailabilities(may10Availabilities)

        when:
        def result = occupation.addBooking(aBookingFor(MAY10))

        then:
        success(result)
    }

    def "Can add booking to cover several availabilities"() {
        Availabilities availabilities = withConcreteAvailabilityList([MAY10, MAY11, MAY12])
        Occupation occupation = withConcreteAvailabilities(availabilities)

        when:
        def result = occupation.addBooking(aBookingFor([MAY10, MAY11].toSet()))

        then:
        success(result)
    }

    def "Can add booking side by side"() {
        given:
        Availabilities availabilities = withConcreteAvailabilityList([MAY10, MAY11, MAY12])
        Occupation occupation = withConcreteAvailabilities(availabilities)

        when:
        def result = occupation.addBooking(aBookingFor(MAY11))

        then:
        success(result)

        when:
        result = occupation.addBooking(aBookingFor(MAY12))

        then:
        success(result)
    }

    def "Cannot add booking that is not continuous availability"() {
        given:
        Availabilities availabilities = withConcreteAvailabilityList([MAY10, MAY11, MAY12])
        Occupation occupation = withConcreteAvailabilities(availabilities)

        when:
        def result = occupation.addBooking(aBookingFor(MAY11))

        then:
        success(result)

        when:
        result = occupation.addBooking(aBookingFor([MAY10, MAY12].toSet()))

        then:
        failure(result)

    }

    def "Cannot add booking in the past"() {

    }

    def "Cannot add booking when availability in the middle is taken"() {
        given:
        Availabilities availabilities = withConcreteAvailabilityList([MAY10, MAY11, MAY12])
        Occupation occupation = withConcreteAvailabilities(availabilities)

        when:
        def result = occupation.addBooking(aBookingFor(MAY11))

        then:
        success(result)

        when:
        result = occupation.addBooking(aBookingFor([MAY10, MAY11, MAY12].toSet()))

        then:
        failure(result)
    }

    def "Cannot add booking twice"() {
        given:
        Availabilities availabilities = withConcreteAvailabilityList([MAY10, MAY11, MAY12])
        Occupation occupation = withConcreteAvailabilities(availabilities)

        when:
        def result = occupation.addBooking(aBookingFor(MAY11))

        then:
        success(result)

        when:
        result = occupation.addBooking(aBookingFor(MAY11))

        then:
        failure(result)
    }

    Booking aBookingFor(Availability availability) {
        new Booking(UUID.randomUUID(), ITEM_ID, availability.getInterval(), [availability.getId()].toSet())
    }

    Booking aBookingFor(Set<Availability> availabilities) {
        Interval intervalCovering = availabilities.stream()
                .map({ avail -> avail.getInterval() })
                .reduce({ int1, int2 -> int1.expand(int2) })
                .get()

        def availabilityIds = availabilities.stream()
                .map({ avail -> avail.getId() })
                .collect(Collectors.toSet())

        new Booking(UUID.randomUUID(), ITEM_ID, intervalCovering, availabilityIds)
    }
}
