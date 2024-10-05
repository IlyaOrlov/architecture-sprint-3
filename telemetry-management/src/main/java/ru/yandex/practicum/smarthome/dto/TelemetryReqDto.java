package ru.yandex.practicum.smarthome.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryReqDto {
    private Long deviceId;
    private Map<String, Object> deviceInfo;
    private LocalDateTime timestamp;
}
