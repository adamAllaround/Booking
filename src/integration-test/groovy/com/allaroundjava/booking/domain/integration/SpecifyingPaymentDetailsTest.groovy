package com.allaroundjava.booking.domain.integration

import com.allaroundjava.booking.DbCleaner
import com.allaroundjava.booking.IntegrationTestConfig
import com.allaroundjava.booking.ReservationDetailsFixtures
import com.allaroundjava.booking.application.AddingPaymentDetails
import com.allaroundjava.booking.assertion.CustomerDetailsAssert
import com.allaroundjava.booking.assertion.PaymentDetailsAssert
import com.allaroundjava.booking.config.BookingsConfig
import com.allaroundjava.booking.config.LoggingConfig
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import spock.lang.Specification

import java.time.LocalDate

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [BookingsConfig, IntegrationTestConfig, LoggingConfig])
@EnableAutoConfiguration
@AutoConfigureEmbeddedDatabase(provider = ZONKY, beanName = "dataSource")
@Sql("/events-db-creation.sql")
class SpecifyingPaymentDetailsTest extends Specification {
    public static final UUID RESERVATION_ID = UUID.randomUUID()

    @Autowired
    private DbCleaner dbCleaner

    @Autowired
    private AddingPaymentDetails addingPaymentDetails

    @Autowired
    private ReservationDetailsFixtures reservationDetailsFixtures

    @Autowired
    private CustomerDetailsAssert customerDetailsAssert

    @Autowired
    private PaymentDetailsAssert paymentDetailsAssert

    def "When Payment details specified then state changes and details persisted"() {
        given:
        reservationDetailsExist()

        when:
        addingPaymentDetails.addPaymentDetails(RESERVATION_ID, "BLIK")

        then:
        paymentDetailsAssert.paymentTypeIs(RESERVATION_ID, "BLIK")
        customerDetailsAssert.statusIsPaymentSpecified(RESERVATION_ID)
    }

    void reservationDetailsExist() {
        reservationDetailsFixtures.reservationInitialized(RESERVATION_ID, LocalDate.of(2022, 10, 10), LocalDate.of(2022, 10, 15))
    }
}
