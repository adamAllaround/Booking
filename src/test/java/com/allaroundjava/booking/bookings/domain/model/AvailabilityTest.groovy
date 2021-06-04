package com.allaroundjava.booking.bookings.domain.model

import spock.lang.Specification

import static com.allaroundjava.booking.bookings.domain.model.Dates2020.*

class AvailabilityTest extends Specification {
    UUID ITEM_ID = UUID.randomUUID()
    Availability existing = Availability.from(ITEM_ID, new Interval(march(10).hour(16), april(10).hour(10)))

    def "No overlap with existing"() {
        when:
        Availability candidate = Availability.from(ITEM_ID, new Interval(january(1).hour(10), february(10).hour(12)))

        then:
        !existing.overlaps(candidate)
    }

    def "Candidate fully covers existing"() {
        when:
        Availability candidate = Availability.from(ITEM_ID, new Interval(march(8).hour(10), april(12).hour(10)))

        then:
        existing.overlaps(candidate)
    }

    def "Candidate is included in existing"() {
        when:
        Availability candidate = Availability.from(ITEM_ID, new Interval(march(16).hour(10), april(1).hour(10)))

        then:
        existing.overlaps(candidate)
    }

    def "Candidate starts before existing and overlaps"() {
        when:
        Availability candidate = Availability.from(ITEM_ID, new Interval(march(10).hour(10), march(10).hour(18)))

        then:
        existing.overlaps(candidate)
    }

    def "Candidate ends after existing and overlaps"() {
        when:
        Availability candidate = Availability.from(ITEM_ID, new Interval(march(20).hour(10), april(20).hour(10)))

        then:
        existing.overlaps(candidate)
    }

    def "Candidate starts at the same hour existing ends"() {
        when:
        Availability candidate = Availability.from(ITEM_ID, new Interval(april(10).hour(10), april(12).hour(10)))

        then:
        existing.overlaps(candidate)
    }

    def "Candidate ends at the same hour existing starts"() {
        when:
        Availability candidate = Availability.from(ITEM_ID, new Interval(february(8).hour(10), march(10).hour(16)))

        then:
        existing.overlaps(candidate)
    }
}
