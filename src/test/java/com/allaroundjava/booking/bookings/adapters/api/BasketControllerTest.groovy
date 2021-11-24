package com.allaroundjava.booking.bookings.adapters.api

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class BasketControllerTest extends Specification {
    private OccupationFacade occupationFacade = Mock()
    private BasketController controller
    private MockMvc mockMvc

    void setup() {
        controller = new BasketController(occupationFacade)
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build()
    }

    def "Successful basket add"() {
        given:
        UUID basketUuid = UUID.randomUUID()

        when:
        occupationFacade.save(_ as BasketController.CreateBasketRequest) >> Optional.of(basketUuid)

        then:
        mockMvc.perform(post("/baskets").contentType(MediaType.APPLICATION_JSON)
                .content(newBasketJson()))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()))
                .andExpect(header().exists("Location"))
    }

    String newBasketJson() {
        '{"itemId" : "b4c548b9-9b90-410e-a21d-d4a9ac7070f3", "dateFrom": "2021-11-21T10:00:00+02:00", "dateTo": "2021-11-22T10:00:00+02:00" }'
    }
}
