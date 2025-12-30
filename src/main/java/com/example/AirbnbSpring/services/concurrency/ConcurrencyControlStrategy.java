package com.example.AirbnbSpring.services.concurrency;

import com.example.AirbnbSpring.models.Availability;

import java.time.LocalDate;
import java.util.List;

public interface ConcurrencyControlStrategy {

    void releaseLock(Long airbnbId, LocalDate checkInDate, LocalDate checkOutDate);

    List<Availability> lockAndCheckAvailability(Long airbnbId, LocalDate checkInDate, LocalDate checkOutDate, Long userId);
}
