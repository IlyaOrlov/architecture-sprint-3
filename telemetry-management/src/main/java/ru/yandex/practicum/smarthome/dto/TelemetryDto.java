package ru.yandex.practicum.smarthome.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TelemetryDto {
    private Long id;
    private Long deviceId;
    private Map<String, Object> deviceInfo;
    private LocalDateTime timestamp;
}
