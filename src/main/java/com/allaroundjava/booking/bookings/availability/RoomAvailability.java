package com.allaroundjava.booking.bookings.availability;

import com.allaroundjava.booking.bookings.shared.Failure;
import com.allaroundjava.booking.bookings.shared.Success;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomAvailability {
    private final SlotRepository slotRepository;
    private final PreBookingRepository preBookingRepository;
    private final ReservationRepository reservationRepository;

    public boolean isAvailable(UUID roomId, LocalDate dateFrom, LocalDate dateTo) {
        ItemTimeSlot slot = slotRepository.slotBetween(roomId, dateFrom, dateTo);
        return slot.isAvailable();
    }

    @Transactional
    public void preBook(UUID reservationId, UUID roomId, LocalDate dateFrom, LocalDate dateTo) {
        if (!isAvailable(roomId, dateFrom, dateTo)) {
            throw new IllegalStateException(String.format("Room %s is already booked", roomId.toString()));
        }

        preBookingRepository.preBook(reservationId, roomId, dateFrom, dateTo);
    }

    @Transactional
    public Either<Failure, Success> book(UUID reservationId) {
        PreBookingDto preBookingDto = preBookingRepository.getByReservationId(reservationId);

        ItemTimeSlot slot = slotRepository.slotBetween(preBookingDto.getRoomId(), preBookingDto.getDateFrom(), preBookingDto.getDateTo());

        if (!slot.isAvailable()) {
            return Either.left(new Failure(String.format("Room %s is not available", preBookingDto.getRoomId())));
        }

        Reservation reservation = slot.book(reservationId);
        reservationRepository.reserve(reservation);

        return Either.right(new Success());
    }
}
