package com.example.AirbnbSpring.saga;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SagaEvent implements Serializable {
    private String sagaId;

    private String eventType;

    private String step;

    private Map<String, Object> payload;

    private LocalDateTime timestamp;

    private String status;

    public enum SagaStatus {
        PENDING, COMPLETED, FAILED, COMPENSATING;
    }
}
