package com.allaroundjava.booking.bookings.adapters.api

import com.allaroundjava.booking.bookings.adapters.db.RoomsDatabaseRepository
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import java.time.Instant
import java.time.OffsetTime
import java.time.ZoneOffset

import static org.hamcrest.CoreMatchers.containsString
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AddRoomControllerTest extends Specification {
    private RoomsDatabaseRepository repository = Mock()
    private AddRoomController addRoomController
    private MockMvc mockMvc

    void setup() {
        addRoomController = new AddRoomController(repository)
        mockMvc = MockMvcBuilders.standaloneSetup(addRoomController).build()
    }

    def "Add new room"() {
        given:
        UUID ownerId = UUID.randomUUID()
        RoomsDatabaseRepository.RoomDatabaseEntity room = new RoomsDatabaseRepository.RoomDatabaseEntity(UUID.randomUUID(),
                ownerId, "Some Room", 3, "Test location", OffsetTime.of(15, 0, 0, 0, ZoneOffset.UTC),
                OffsetTime.of(10, 0, 0, 0, ZoneOffset.UTC), Instant.now())

        when:
        repository.save(_ as RoomsDatabaseRepository.RoomDatabaseEntity) >> room

        then:
        mockMvc.perform(post("/owners/${ownerId}/items").contentType(MediaType.APPLICATION_JSON)
                .content('{"name" : "Some Room", "capacity": 3, "location": "Test location", ' +
                        '"hotelHourStart": "15:00:00+02:00" , "hotelHourEnd": "10:00:00+02:00" }'))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(content().string(containsString("Some Room")))
    }
}
