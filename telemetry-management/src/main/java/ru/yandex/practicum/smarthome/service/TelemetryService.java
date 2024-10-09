package ru.yandex.practicum.smarthome.service;

import ru.yandex.practicum.smarthome.dto.TelemetryDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface TelemetryService {
    public TelemetryDto addTelemetry(Long deviceId, Map<String, Object> deviceInfo, LocalDateTime timestamp);
    public TelemetryDto getLatestTelemetry(Long deviceId);
    public List<TelemetryDto> getTelemetryHistory(Long deviceId, LocalDateTime start, LocalDateTime end);
}
