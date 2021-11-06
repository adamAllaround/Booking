package com.allaroundjava.booking.bookings.adapters.api

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.hamcrest.CoreMatchers.containsString
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class BookingsControllerTest extends Specification {
    private static final ITEM_ID = UUID.randomUUID()
    private OccupationFacade occupationFacade = Mock()
    private BookingsController controller
    private MockMvc mockMvc

    void setup() {
        controller = new BookingsController(occupationFacade)
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build()
    }

    def "Successful booking add"() {
        def bookingId = UUID.randomUUID()

        when:
        occupationFacade.saveBooking(_ as BookingRequest) >> Optional.of(mockBookingResponse(bookingId))

        then:
        mockMvc.perform(post("/items/${ITEM_ID}/bookings").contentType(MediaType.APPLICATION_JSON)
                .content(newBookingJson()))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(header().string("location", containsString(bookingId.toString())))
    }

    def "Unsuccessful booking add"() {
        when:
        occupationFacade.saveBooking(_ as BookingRequest) >> Optional.empty()

        then:
        mockMvc.perform(post("/items/${ITEM_ID}/bookings").contentType(MediaType.APPLICATION_JSON)
                .content(newBookingJson()))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
    }

    private static BookingResponse mockBookingResponse(UUID bookingId) {
        return new BookingResponse(bookingId)
    }

    private static String newBookingJson() {
        '{"firstName":"Josh", "lastName":"Johnson"}'
    }
}
