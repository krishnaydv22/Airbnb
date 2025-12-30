package com.example.AirbnbSpring.services.handlers;

import com.example.AirbnbSpring.models.Booking;
import com.example.AirbnbSpring.repositories.reads.RedisWriteRepository;
import com.example.AirbnbSpring.repositories.writes.BookingWriteRepository;
import com.example.AirbnbSpring.saga.SagaEvent;
import com.example.AirbnbSpring.saga.SagaEventPublisher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BookingEventHandler {

    private final BookingWriteRepository bookingWriteRepository;
    private final SagaEventPublisher sagaEventPublisher;
    private final RedisWriteRepository redisWriteRepository;

    public void handleBookingConfirmRequested(SagaEvent sagaEvent){
      try{
          Map<String,Object> payload = sagaEvent.getPayload();
          Long bookingId = Long.valueOf(payload.get("bookingId").toString());
          Long airbnbId = Long.valueOf(payload.get("airbnbId").toString());

          LocalDate checkInDate = LocalDate.parse(payload.get("checkIndate").toString());
          LocalDate checkOutDate = LocalDate.parse(payload.get("checkOutDate").toString());
          Booking booking = bookingWriteRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("booking not found "));
          booking.setBookingStatus(Booking.BookingStatus.CONFIRMED);
          bookingWriteRepository.save(booking);

          // update it to redis
          redisWriteRepository.writeBookingReadModel(booking);


          sagaEventPublisher.publishEvent("BOOKING_CONFIRMED","CONFIRM_BOOKING",
                  Map.of("bookingId", bookingId, "airbnbId", airbnbId, "checkInDate", checkInDate.toString(), "checkOutDate", checkOutDate.toString())
          );
      }catch (Exception e){

          Map<String, Object> payload = sagaEvent.getPayload();
          sagaEventPublisher.publishEvent("BOOKING_COMPENSATED", "COMPENSATE_BOOKING", payload);
          throw new RuntimeException("Failed to confirm booking", e);
      }


    }

    @Transactional
    public void handleBookingCancelRequested(SagaEvent sagaEvent) {
        try {
            Map<String, Object> payload = sagaEvent.getPayload();
            Long bookingId = Long.valueOf(payload.get("bookingId").toString());
            Long airbnbId = Long.valueOf(payload.get("airbnbId").toString());
            LocalDate checkInDate = LocalDate.parse(payload.get("checkInDate").toString());
            LocalDate checkOutDate = LocalDate.parse(payload.get("checkOutDate").toString());

            Booking booking = bookingWriteRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
            booking.setBookingStatus(Booking.BookingStatus.CANCELLED);
            bookingWriteRepository.save(booking);

            sagaEventPublisher.publishEvent("BOOKING_CANCELLED","CANCEL_BOOKING",
                    Map.of("bookingId", bookingId, "airbnbId", airbnbId, "checkInDate", checkInDate.toString(), "checkOutDate", checkOutDate.toString())
            );

        } catch (Exception e) {
            Map<String, Object> payload = sagaEvent.getPayload();
            sagaEventPublisher.publishEvent("BOOKING_COMPENSATED", "COMPENSATE_BOOKING", payload);
            // TODO: handle compensation -> add the current booking status also
            throw new RuntimeException("Failed to cancel booking", e);
        }
    }


}
