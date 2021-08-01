package com.allaroundjava.booking.common.events;

import com.allaroundjava.booking.bookings.domain.model.Interval;
import lombok.Data;

import java.time.OffsetTime;
import java.util.Set;
import java.util.UUID;

class EventPayload {
    @Data
    static class HotelRoom {
        OffsetTime hotelHourStart;
        OffsetTime hotelHourEnd;
    }

    @Data
    static class Booking {
        UUID itemId;
        UUID bookingId;
        Interval interval;
        Set<UUID> availabilityIds;
    }
}