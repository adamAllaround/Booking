package com.allaroundjava.booking.bookings.adapters.api

import com.allaroundjava.booking.bookings.domain.command.AddBasketCommand
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import java.time.OffsetDateTime
import java.time.ZoneOffset

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
        occupationFacade.save(_ as AddBasketCommand) >> successfulAddBasketResponse(basketUuid)

        then:
        mockMvc.perform(post("/baskets").contentType(MediaType.APPLICATION_JSON)
                .content(newBasketJson()))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(header().exists("Location"))
    }

    String newBasketJson() {
        '{"roomId" : "b4c548b9-9b90-410e-a21d-d4a9ac7070f3", "dateStart": "2021-11-21T10:00:00+02:00", "dateEnd": "2021-11-22T10:00:00+02:00" }'
    }


    Optional<BasketController.AddBasketResponse> successfulAddBasketResponse(UUID uuid) {
        Optional.of(new BasketController.AddBasketResponse(uuid,
                OffsetDateTime.of(2021,11,28, 10,0,0,0, ZoneOffset.UTC),
                OffsetDateTime.of(2021,11,29, 10,0,0,0, ZoneOffset.UTC)))
    }
}
