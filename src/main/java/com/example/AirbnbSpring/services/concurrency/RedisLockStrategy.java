package com.example.AirbnbSpring.services.concurrency;

import com.example.AirbnbSpring.models.Availability;
import com.example.AirbnbSpring.repositories.writes.AvailabilityWriteRepository;
import com.example.AirbnbSpring.services.concurrency.ConcurrencyControlStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisLockStrategy implements ConcurrencyControlStrategy {

    private  static final String LOCK_KEY_PREFIX = "lock:availability:";
    private static final Duration LOCK_TIMEOUT = Duration.ofMinutes(2);

    private final RedisTemplate<String,String> redisTemplate;
    private final AvailabilityWriteRepository availabilityWriteRepository;



    @Override
    public void releaseLock(Long airbnbId, LocalDate checkInDate, LocalDate checkOutDate) {

       String lockkey = generateLockKey(airbnbId,checkInDate,checkOutDate);

           redisTemplate.delete(lockkey);


    }

    @Override
    public List<Availability> lockAndCheckAvailability(Long airbnbId, LocalDate checkInDate,LocalDate checkOutDate, Long userId) {
       Long bookedSlotes =  availabilityWriteRepository.countByAirbnbIdAndDateBetweenAndBookingIdIsNotNull(airbnbId,checkInDate,checkOutDate);

       if (bookedSlotes > 0){
           throw new RuntimeException("Airbnb is not available for given all Dates.");
       }

        String key = generateLockKey(airbnbId,checkInDate,checkOutDate);
       boolean lock = redisTemplate.opsForValue().setIfAbsent(key,userId.toString(),LOCK_TIMEOUT);
       if(!lock){

           throw new IllegalStateException("Failed to aquire a lock on given dates");
       }

       try{
          return availabilityWriteRepository.findByAirbnbIdAndDateBetween(airbnbId,checkInDate,checkOutDate);

       }catch (Exception e){

           releaseLock(airbnbId,checkInDate,checkOutDate);
           throw e;
       }

    }

    private String generateLockKey(Long airbndId, LocalDate checkInDate, LocalDate checkOutDate){

      return LOCK_KEY_PREFIX + airbndId + ":" + checkInDate + ":" + checkOutDate;


    }
}
