package com.allaroundjava.booking.bookings.adapters.api

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset

import static org.hamcrest.CoreMatchers.containsString
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AvailabilitiesControllerTest extends Specification {
    private AvailabilitiesFacade availabilitiesFacade = Mock()
    private OccupationFacade occupationFacade = Mock()
    private AvailabilitiesController controller
    private MockMvc mockMvc

    void setup() {
        controller = new AvailabilitiesController(availabilitiesFacade, occupationFacade)
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build()
    }

    def "Get all availabilities for item"() {
        def itemId = UUID.randomUUID()
        def availabilityId = UUID.randomUUID()
        List<AvailabilityResponse> availabilityResponseList = [new AvailabilityResponse(availabilityId, itemId,
                OffsetDateTime.of(LocalDateTime.of(2020, 5, 21, 10, 0, 0), ZoneOffset.UTC), OffsetDateTime.of(LocalDateTime.of(2020, 5, 21, 11, 0, 0), ZoneOffset.UTC))]
        AvailabilitiesResponse response = new AvailabilitiesResponse(availabilityResponseList)

        when:
        availabilitiesFacade.getAllByItemId(itemId) >> response

        then:
        mockMvc.perform(get("/items/${itemId}/availabilities").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().string(containsString(availabilityId.toString())))
    }

    def "Successful availability add"() {
        def itemId = UUID.randomUUID()
        def availabilityResponse = new AvailabilityResponse(UUID.randomUUID(), itemId,
                OffsetDateTime.of(LocalDateTime.of(2020, 5, 21, 10, 0, 0), ZoneOffset.UTC), OffsetDateTime.of(LocalDateTime.of(2020, 5, 21, 11, 0, 0), ZoneOffset.UTC))
        when:
        occupationFacade.save(itemId, _ as AvailabilityRequest) >> Optional.of(new AvailabilitiesResponse([availabilityResponse]))

        then:
        mockMvc.perform(post("/items/${itemId}/availabilities").contentType(MediaType.APPLICATION_JSON)
                .content(newAvailabilityJson()))
                .andExpect(status().is(HttpStatus.CREATED.value()))
    }

    def "Unsuccessful availability add"() {
        def itemId = UUID.randomUUID()

        when:
        occupationFacade.save(itemId, _ as AvailabilityRequest) >> Optional.empty()

        then:
        mockMvc.perform(post("/items/${itemId}/availabilities").contentType(MediaType.APPLICATION_JSON)
                .content(newAvailabilityJson()))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
    }

    private static String newAvailabilityJson() {
        '{"start":"2021-05-21T10:15:30+01:00","end":"2021-05-21T10:30:30+01:00"}'
    }

}
