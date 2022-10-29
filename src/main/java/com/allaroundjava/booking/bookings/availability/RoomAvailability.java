package com.allaroundjava.booking.bookings.availability;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomAvailability {
    private final SlotRepository slotRepository;

    public boolean isAvailable(UUID roomId, LocalDate dateFrom, LocalDate dateTo) {
        ItemTimeSlot slot = slotRepository.slotBetween(roomId, dateFrom, dateTo);
        return slot.isAvailable();
    }
}
