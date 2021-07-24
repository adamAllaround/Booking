package com.allaroundjava.booking.common.events;

import lombok.Data;

import java.time.OffsetTime;

class EventPayload {
    @Data
    static class HotelRoom {
        OffsetTime hotelHourStart;
        OffsetTime hotelHourEnd;
    }
}
