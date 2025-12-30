package com.example.AirbnbSpring.saga;


import com.example.AirbnbSpring.services.handlers.AvailabilityEventHandler;
import com.example.AirbnbSpring.services.handlers.BookingEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SagaEventProcessor {

    private final BookingEventHandler bookingEventHandler;
    private final AvailabilityEventHandler availabilityEventHandler;

    public void processEvent(SagaEvent sagaEvent) {
        switch (sagaEvent.getEventType()) {
            case "BOOKING_CREATED":
                // no action
                break;
            case "BOOKING_CONFIRM_REQUESTED":
                log.info("saga event from processor {}", sagaEvent.getSagaId());
                bookingEventHandler.handleBookingConfirmRequested(sagaEvent);
                break;
            case "BOOKING_CONFIRMED":
                availabilityEventHandler.handleBookingConfirmed(sagaEvent);
                log.info("Booking confirmed for booking id: {}", sagaEvent.getPayload().get("bookingId"));
                break;
            case "BOOKING_CANCEL_REQUESTED":
                bookingEventHandler.handleBookingCancelRequested(sagaEvent);
                break;
            case "BOOKING_CANCELLED":
                availabilityEventHandler.handleBookingCancelled(sagaEvent);
                log.info("Booking cancelled for booking id: {}", sagaEvent.getPayload().get("bookingId"));
                break;
            case "BOOKING_COMPENSATED":
                log.info("Booking compensated for booking id: {}", sagaEvent.getPayload().get("bookingId"));
                break;
            default:
                break;
        }
    }
}
