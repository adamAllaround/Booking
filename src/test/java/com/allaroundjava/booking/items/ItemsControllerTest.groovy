package com.allaroundjava.booking.items

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.ZoneOffset

import static org.hamcrest.CoreMatchers.containsString
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ItemsControllerTest extends Specification {
    private ItemsService itemsService = Mock()
    private ItemsController itemsController
    private MockMvc mockMvc

    void setup() {
        itemsController = new ItemsController(itemsService)
        mockMvc = MockMvcBuilders.standaloneSetup(itemsController).build()
    }

    def "Get All Items for given Owner"() {
        given:
        Owner owner = new Owner(UUID.randomUUID(), LocalDateTime.of(2021, 5,11,10,0).toInstant(ZoneOffset.UTC))
        when:
        itemsService.getAllByOwnerId(owner.getId()) >>
                [new Item(id: UUID.randomUUID(), ownerId: owner.getId(), name: "Some Item",capacity: 10, location: "Warsaw")]
        then:
        mockMvc.perform(get("/owners/${owner.id}/items").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().string(containsString("Some Item")))
    }

    def "Add new item"() {
        given:
        UUID ownerId = UUID.randomUUID()
        Item item = new Item(ownerId: ownerId,  name: "Some Item", location: "Test location", capacity: 3, type: "HotelRoom")

        when:
        itemsService.save(_ as Item) >> item

        then:
        mockMvc.perform(post("/owners/${ownerId}/items").contentType(MediaType.APPLICATION_JSON)
                .content('{"name" : "Some Item", "details": {"type" : "hotelRoom", "capacity": 3, "location" : "Test location"}}'))
        .andExpect(status().is(HttpStatus.CREATED.value()))
        .andExpect(content().string(containsString("Some Item")))
    }
}
