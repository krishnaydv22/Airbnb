package com.example.AirbnbSpring.controllers;

import com.example.AirbnbSpring.dto.CreateBookingRequest;
import com.example.AirbnbSpring.dto.UpdateBookingRequest;
import com.example.AirbnbSpring.models.Booking;
import com.example.AirbnbSpring.services.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody CreateBookingRequest createBookingRequest) {
        return ResponseEntity.ok(bookingService.createBooking(createBookingRequest));
    }

    @PutMapping
    public ResponseEntity<Booking> updateBooking(@RequestBody UpdateBookingRequest updateBookingRequest) {
        return ResponseEntity.ok(bookingService.updateBooking(updateBookingRequest));
    }
}
