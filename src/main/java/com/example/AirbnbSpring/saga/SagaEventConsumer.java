package com.example.AirbnbSpring.saga;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class SagaEventConsumer {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String SAGA_QUEUE = "saga:events";
    private final ObjectMapper objectMapper;
    private final SagaEventProcessor eventProcessor;

    @Scheduled(fixedDelay = 500) // polls every events in every 500ms
    public void consume(){
       String eventJson =  redisTemplate.opsForList().leftPop(SAGA_QUEUE, 1, TimeUnit.DAYS.SECONDS);
       try {
           if (eventJson != null && !eventJson.isEmpty()) {
               SagaEvent sagaEvent = objectMapper.readValue(eventJson, SagaEvent.class);
               log.info("proccessing {} saga event ", sagaEvent.getSagaId());
               eventProcessor.processEvent(sagaEvent);
               log.info("saga event processed ");

           }
       }catch(Exception e){
         log.error("error processing saga event ", e.getMessage());
         throw new RuntimeException("error processing saga event",e);
       }

    }
}
