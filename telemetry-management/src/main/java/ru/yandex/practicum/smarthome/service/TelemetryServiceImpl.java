package ru.yandex.practicum.smarthome.service;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.smarthome.dto.TelemetryDto;
import ru.yandex.practicum.smarthome.entity.Telemetry;
import ru.yandex.practicum.smarthome.repository.TelemetryRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TelemetryServiceImpl implements TelemetryService {

    private final TelemetryRepository telemetryRepository;

    @Override
    public TelemetryDto addTelemetry(Long deviceId, Map<String, Object> deviceInfo, LocalDateTime timestamp) {
        Telemetry telemetry = new Telemetry();
        telemetry.setDeviceId(deviceId);
        telemetry.setDeviceInfo(deviceInfo);
        telemetry.setTimestamp(timestamp);

        telemetry = telemetryRepository.save(telemetry);
        return convertToDto(telemetry);
    }

    @Override
    public TelemetryDto getLatestTelemetry(Long deviceId) {
        Optional<Telemetry> telemetry = telemetryRepository.findFirstByDeviceIdOrderByTimestampDesc(deviceId);
        if (telemetry.isPresent()) {
            Telemetry latestTelemetry = telemetry.get();
            return convertToDto(latestTelemetry);
        }
        return null;
    }

    @Override
    public List<TelemetryDto> getTelemetryHistory(Long deviceId, LocalDateTime start, LocalDateTime end) {
        List<Telemetry> telemetryList = telemetryRepository.findAllByDeviceIdAndTimestampBetween(deviceId, start, end);
        return telemetryList.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    private TelemetryDto convertToDto(Telemetry telemetry) {
        TelemetryDto dto = new TelemetryDto();
        dto.setId(telemetry.getId());
        dto.setDeviceId(telemetry.getDeviceId());
        dto.setDeviceInfo(telemetry.getDeviceInfo());
        dto.setTimestamp(telemetry.getTimestamp());
        return dto;
    }
}
