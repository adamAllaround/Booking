package com.allaroundjava.booking.bookings.domain.model

import spock.lang.Specification

import java.util.stream.Collectors

import static RoomFixture.withClock
import static com.allaroundjava.booking.bookings.domain.model.AvailabilityFixture.*
import static com.allaroundjava.booking.bookings.domain.model.CommandResult.failure
import static com.allaroundjava.booking.bookings.domain.model.CommandResult.success
import static com.allaroundjava.booking.bookings.domain.model.Dates2020.JAN_CLOCK

class OccupationBookingTest extends Specification {
    private static final UUID ITEM_ID = UUID.randomUUID()

    def "Can add booking to cover existing availability"() {
        RoomOccupation room = withClock(JAN_CLOCK).andAvailabilitiesInIntervals([MAY10.interval])

        when:
        def result = room.addBooking(aBookingFor(MAY10))

        then:
        success(result)
    }

    def "Can add booking to cover several availabilities"() {
        RoomOccupation room = withClock(JAN_CLOCK).andAvailabilitiesInIntervals([MAY10.interval, MAY11.interval, MAY12.interval])

        when:
        def result = room.addBooking(aBookingFor([MAY10, MAY11].toSet()))

        then:
        success(result)
    }

    def "Can add booking side by side"() {
        given:
        RoomOccupation room = withClock(JAN_CLOCK).andAvailabilitiesInIntervals([MAY10.interval, MAY11.interval, MAY12.interval])

        when:
        def result = room.addBooking(aBookingFor(MAY11))

        then:
        success(result)

        when:
        result = room.addBooking(aBookingFor(MAY12))

        then:
        success(result)
    }

    def "Cannot add booking that is not continuous availability"() {
        given:
        RoomOccupation room = withClock(JAN_CLOCK).andAvailabilitiesInIntervals([MAY10.interval, MAY11.interval, MAY12.interval])

        when:
        def result = room.addBooking(aBookingFor(MAY11))

        then:
        success(result)

        when:
        result = room.addBooking(aBookingFor([MAY10, MAY12].toSet()))

        then:
        failure(result)

    }

    def "Cannot add booking in the past"() {
        given:
        RoomOccupation room = withClock(JAN_CLOCK).andAvailabilitiesInIntervals([MAY10.interval, MAY11.interval, MAY12.interval])

        when:
        def result = room.addBooking(aBookingFor(MAY11))

        then:
        failure(result)
    }

    def "Cannot add booking when availability in the middle is taken"() {
        given:
        RoomOccupation room = withClock(JAN_CLOCK).andAvailabilitiesInIntervals([MAY10.interval, MAY11.interval, MAY12.interval])

        when:
        def result = room.addBooking(aBookingFor(MAY11))

        then:
        success(result)

        when:
        result = room.addBooking(aBookingFor([MAY10, MAY11, MAY12].toSet()))

        then:
        failure(result)
    }

    def "Cannot add booking twice"() {
        given:
        RoomOccupation room = withClock(JAN_CLOCK).andAvailabilitiesInIntervals([MAY10.interval, MAY11.interval, MAY12.interval])

        when:
        def result = room.addBooking(aBookingFor(MAY11))

        then:
        success(result)

        when:
        result = room.addBooking(aBookingFor(MAY11))

        then:
        failure(result)
    }

    Booking aBookingFor(Availability availability) {
        new Booking(UUID.randomUUID(), ITEM_ID, "test@booker.email", availability.getInterval(), [availability.getId()].toSet())
    }

    Booking aBookingFor(Set<Availability> availabilities) {
        Interval intervalCovering = availabilities.stream()
                .map({ avail -> avail.getInterval() })
                .reduce({ int1, int2 -> int1.expand(int2) })
                .get()

        def availabilityIds = availabilities.stream()
                .map({ avail -> avail.getId() })
                .collect(Collectors.toSet())

        new Booking(UUID.randomUUID(), ITEM_ID,"test@booker.email", intervalCovering, availabilityIds)
    }
}
