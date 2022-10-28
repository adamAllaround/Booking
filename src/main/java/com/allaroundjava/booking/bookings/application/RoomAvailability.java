package com.allaroundjava.booking.bookings.application;

import com.allaroundjava.booking.bookings.domain.availability.ItemTimeSlot;
import com.allaroundjava.booking.bookings.domain.ports.SlotRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@RequiredArgsConstructor
public class RoomAvailability {
    private final SlotRepository slotRepository;

    public boolean isAvailable(UUID roomId, LocalDate dateFrom, LocalDate dateTo) {
        ItemTimeSlot slot = slotRepository.slotBetween(roomId, dateFrom, dateTo);
        return slot.isAvailable();
    }
}
