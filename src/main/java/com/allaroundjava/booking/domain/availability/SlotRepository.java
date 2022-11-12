package com.allaroundjava.booking.domain.availability;

import java.time.LocalDate;
import java.util.UUID;

interface SlotRepository {
    ItemTimeSlot slotBetween(UUID roomId, LocalDate dateFrom, LocalDate dateTo);
}
