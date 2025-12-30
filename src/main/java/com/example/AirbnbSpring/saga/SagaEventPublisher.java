package com.example.AirbnbSpring.saga;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SagaEventPublisher {

  private static final String SAGA_QUEUE = "saga:events";
  private final RedisTemplate<String, String> redisTemplate;
  private final ObjectMapper objectMapper;

  public void publishEvent(String eventType,  String step, Map<String,Object> payload){

      SagaEvent sagaEvent = SagaEvent.builder()
              .sagaId(UUID.randomUUID().toString())
              .eventType(eventType)
              .step(step)
              .payload(payload)
              .timestamp(LocalDateTime.now())
              .status(SagaEvent.SagaStatus.PENDING.name())
              .build();

      try{
          String eventJson = objectMapper.writeValueAsString(sagaEvent);
          redisTemplate.opsForList().rightPush(SAGA_QUEUE, eventJson);

      }catch(Exception e){
          throw new RuntimeException("Failed to publish saga event", e);

      }

  }


}
