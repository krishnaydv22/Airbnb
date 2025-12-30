package com.example.AirbnbSpring.repositories.reads;

import com.example.AirbnbSpring.models.Booking;
import com.example.AirbnbSpring.models.readModels.BookingReadModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;


@RequiredArgsConstructor
@Repository
public class RedisWriteRepository {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String ,String> redisTemplate;

    public void writeBookingReadModel(Booking booking) {
        BookingReadModel bookingReadModel = BookingReadModel.builder()
                .id(booking.getId())
                .airbnbId(booking.getAirbnbId())
                .userId(booking.getUserId())
                .totalPrice(booking.getTotalPrice())
                .bookingStatus(booking.getBookingStatus().name())
                .idempotencyKey(booking.getIdempotencyKey())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .build();
try{
    saveBookingReadModel(bookingReadModel);


}catch (Exception e){
    throw new IllegalStateException(e);
}
    }

    private void saveBookingReadModel(BookingReadModel bookingReadModel) throws JsonProcessingException {

        String key = RedisReadRepository.BOOKING_KEY_PREFIX + bookingReadModel.getId();
        String value = objectMapper.writeValueAsString(bookingReadModel);
        redisTemplate.opsForValue().set(key, value);



    }
}
