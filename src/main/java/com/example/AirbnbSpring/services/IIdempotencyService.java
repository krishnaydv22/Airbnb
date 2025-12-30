package com.example.AirbnbSpring.services;

import com.example.AirbnbSpring.models.Booking;

import java.util.Optional;

public interface IIdempotencyService {

    boolean isIdempotencyKeyUsed(String idempotencyKey);

    Optional<Booking> findBookingByIdempotencyKey(String idempotencyKey);


}
