package com.allaroundjava.booking.bookings.application


import com.allaroundjava.booking.IntegrationTestConfig
import com.allaroundjava.booking.ReservationDetailsFixtures
import com.allaroundjava.booking.assertion.CustomerDetailsAssert
import com.allaroundjava.booking.bookings.config.BookingsConfig
import com.allaroundjava.booking.bookings.details.CustomerDetails
import com.allaroundjava.booking.common.LoggingConfig
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import spock.lang.Specification

import java.time.LocalDate

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY

@EnableAutoConfiguration
@ContextConfiguration(classes = [BookingsConfig, IntegrationTestConfig, LoggingConfig])
@AutoConfigureEmbeddedDatabase(provider = ZONKY, beanName = "dataSource")
@Sql("/events-db-creation.sql")
class AddingCustomerDetailsIntegrationTest extends Specification {

    public static final UUID RESERVATION_ID = UUID.randomUUID()

    @Autowired
    private AddingCustomerDetails addingCustomerDetails

    @Autowired
    private CustomerDetailsAssert customerDetailsAssert

    @Autowired
    private ReservationDetailsFixtures reservationDetailsFixtures

    def "When Adding customer details then details present"() {

        given:
        def customerDetails = new CustomerDetails("Jan", "Nowak", "email@gmail.com", "00481234455")
        and:
        reservationDetailsExist()

        when:
        addingCustomerDetails.addCustomerDetails(RESERVATION_ID, customerDetails)

        then:
        customerDetailsAssert.hasCustomerDetailsFor(RESERVATION_ID)
        customerDetailsAssert.emailIs(RESERVATION_ID, "email@gmail.com")
        customerDetailsAssert.statusIsCustomerSpecified(RESERVATION_ID)
    }

    void reservationDetailsExist() {
        reservationDetailsFixtures.reservationInitialized(RESERVATION_ID, LocalDate.of(2022, 10, 10), LocalDate.of(2022, 10, 15))
    }
}