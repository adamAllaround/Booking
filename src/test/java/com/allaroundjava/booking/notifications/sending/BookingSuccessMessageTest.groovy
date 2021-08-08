package com.allaroundjava.booking.notifications.sending

import com.allaroundjava.booking.notifications.Interval
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.ZoneOffset

class BookingSuccessMessageTest extends Specification {
    def "something"() {
        when:
        def message = new BookingSuccessMessage(UUID.randomUUID(),
                "some@email",
                "receiver@email.com",
                new Interval(LocalDateTime.of(2021, 8, 8, 10, 0, 0).toInstant(ZoneOffset.UTC) //TODO need to figure out the right timezone here
                        , LocalDateTime.of(2021, 8, 11, 10, 0, 0).toInstant(ZoneOffset.UTC)),
                3)
        println message.getContent()

        then:
        1 == 1
    }
}
