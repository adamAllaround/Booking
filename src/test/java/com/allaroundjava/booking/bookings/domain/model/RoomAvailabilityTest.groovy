package com.allaroundjava.booking.bookings.domain.model

import spock.lang.Specification

import java.time.Instant
import java.time.temporal.ChronoField

import static com.allaroundjava.booking.bookings.domain.model.CommandResult.failure
import static com.allaroundjava.booking.bookings.domain.model.CommandResult.success
import static com.allaroundjava.booking.bookings.domain.model.Dates2020.JAN_CLOCK
import static com.allaroundjava.booking.bookings.domain.model.Dates2020.march
import static RoomFixture.withClock
import static com.allaroundjava.booking.bookings.domain.model.Dates2020.may

class RoomAvailabilityTest extends Specification {
    private static UUID ITEM_ID = UUID.randomUUID()

    def "Can add all availabilities when empty"() {
        RoomOccupation room = RoomFixture.empty()
        def candidateInterval = new Interval(may(20).hour(10), may(23).hour(10))

        when:
        def result = room.addAvailability(candidateInterval)

        then:
        success(result)

        and:

        def resultList = result.get().availabilityList
        isStandardHotelNightAvailabilityOn(resultList.get(0), 20)
        isStandardHotelNightAvailabilityOn(resultList.get(1), 21)
        isStandardHotelNightAvailabilityOn(resultList.get(2), 22)
    }

    def "Can add availability when beginning of interval taken"() {
        RoomOccupation room = withClock(JAN_CLOCK)
                .andAvailabilityBetween(may(20).hour(15), may(21).hour(10))

        when:
        def result = room.addAvailability(new Interval(may(20).hour(10), may(22).hour(10)))

        then:
        success(result)

        and:
        def resultList = result.get().availabilityList
        resultList.size() == 1
        isStandardHotelNightAvailabilityOn(resultList.get(0), 21)
    }

    def "Can add availability when beginning and end of interval taken"() {
        RoomOccupation room = withClock(JAN_CLOCK).andAvailabilitiesInIntervals([
                new Interval(may(20).hour(15), may(21).hour(10)),
                new Interval(may(22).hour(15), may(23).hour(10))
        ])

        when:
        def result = room.addAvailability(new Interval(may(20).hour(10), may(24).hour(10)))

        then:
        success(result)

        and:
        def resultList = result.get().availabilityList
        resultList.size() == 2
        isStandardHotelNightAvailabilityOn(resultList.get(0), 21)
        isStandardHotelNightAvailabilityOn(resultList.get(1), 23)
    }

    def "Can add availabilities around existing availability when interval big enough"() {
        RoomOccupation room = withClock(JAN_CLOCK)
                .andAvailabilityBetween(may(20).hour(10), may(21).hour(10))

        when:
        def result = room.addAvailability(new Interval(may(19).hour(13), may(22).hour(10)))

        then:
        success(result)

        and:
        def resultList = result.get().availabilityList
        resultList.size() == 2
        isStandardHotelNightAvailabilityOn(resultList.get(0), 19)
        isStandardHotelNightAvailabilityOn(resultList.get(1), 21)
    }

    def "Cannot add availability when interval fully covered with existing availabilities"() {
        RoomOccupation room = withClock(JAN_CLOCK)
                .andAvailabilityBetween(may(20).hour(10), may(21).hour(10))

        when:
        def result = room.addAvailability(new Interval(may(20).hour(13), may(21).hour(12)))

        then:
        failure(result)
    }

    def "Can add availability to empty occupation"() {
        given:
        RoomOccupation room = withClock(JAN_CLOCK).empty()

        when:
        def result = room.addAvailability(new Interval(march(13).hour(15), march(14).hour(10)))

        then:
        success(result)
    }

    def "Cannot add overlapping availability"() {
        given:
        RoomOccupation room = withClock(JAN_CLOCK)
                .andAvailabilityBetween(march(13).hour(16), march(14).hour(16))


        when:
        def result = room.addAvailability(new Interval(march(13).hour(12), march(14).hour(12)))

        then:
        failure(result)
    }

    def "Can remove existing availability"() {
        given:
        RoomOccupation room = withClock(JAN_CLOCK).empty()

        and:
        def addResult = room.addAvailability(new Interval(march(10).hour(12), march(11).hour(13)))

        when:
        def result = room.removeAvailability(addResult.map({ success -> success.availabilityList }).get().get(0))

        then:
        success(result)
    }

    def "Cannot remove non-existent availability"() {
        given:
        RoomOccupation room = withClock(JAN_CLOCK)
                .andAvailabilityBetween(march(10).hour(12), march(14).hour(12))

        when:
        def result = room.removeAvailability(Availability.from(ITEM_ID, new Interval(march(13).hour(7), march(17).hour(7))))

        then:
        failure(result)
    }

    def "Can Remove availability add add it afterwards in same slot"() {
        given:
        RoomOccupation room = withClock(JAN_CLOCK).empty()

        and:
        room.addAvailability(new Interval(march(10).hour(12), march(13).hour(13))).get().availabilityList
        def toRemove = room.addAvailability(new Interval(march(13).hour(14), march(14).hour(15))).get().availabilityList

        when:
        def result = room.removeAvailability(toRemove.get(0))

        then:
        success(result)

        and:
        success(room.addAvailability(new Interval(march(13).hour(15), march(15).hour(15))))
    }

    void isStandardHotelNightAvailabilityOn(Availability availability, int startDay) {
        assert availability.start == Instant.from(may(startDay).hour(RoomFixture.STANDARD_HOTEL_START.get(ChronoField.HOUR_OF_DAY)))
        assert availability.end == Instant.from(may(startDay + 1).hour(RoomFixture.STANDARD_HOTEL_END.get(ChronoField.HOUR_OF_DAY)))
    }
}
