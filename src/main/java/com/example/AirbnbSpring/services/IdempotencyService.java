package com.example.AirbnbSpring.services;

import com.example.AirbnbSpring.models.Booking;
import com.example.AirbnbSpring.models.readModels.BookingReadModel;
import com.example.AirbnbSpring.repositories.reads.RedisReadRepository;
import com.example.AirbnbSpring.repositories.writes.BookingWriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IdempotencyService implements IIdempotencyService{

    private final RedisReadRepository redisReadRepository;
    private final BookingWriteRepository bookingWriteRepository;
    @Override
    public boolean isIdempotencyKeyUsed(String idempotencyKey) {

        return false;
    }

    @Override
    public Optional<Booking> findBookingByIdempotencyKey(String idempotencyKey) {
        BookingReadModel bookingReadModel = redisReadRepository.findBookingByIdempotencyKey(idempotencyKey);

        if(bookingReadModel != null) {
            // TODO: move it to a mapper/adapter
            Booking booking = Booking.builder()
                    .id(bookingReadModel.getId())
                    .airbnbId(bookingReadModel.getAirbnbId())
                    .userId(bookingReadModel.getUserId())
                    .totalPrice(bookingReadModel.getTotalPrice())
                    .bookingStatus(Booking.BookingStatus.valueOf(bookingReadModel.getBookingStatus()))
                    .idempotencyKey(bookingReadModel.getIdempotencyKey())
                    .checkInDate(bookingReadModel.getCheckInDate())
                    .checkOutDate(bookingReadModel.getCheckOutDate())
                    .build();

            return Optional.of(booking);
        }

        return bookingWriteRepository.findByIdempotencyKey(idempotencyKey);
    }
}
