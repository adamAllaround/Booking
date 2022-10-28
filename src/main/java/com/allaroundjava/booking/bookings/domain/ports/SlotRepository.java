package com.allaroundjava.booking.bookings.domain.ports;

import com.allaroundjava.booking.bookings.domain.availability.ItemTimeSlot;

import java.time.LocalDate;
import java.util.UUID;

public interface SlotRepository {
    ItemTimeSlot slotBetween(UUID roomId, LocalDate dateFrom, LocalDate dateTo);
}
