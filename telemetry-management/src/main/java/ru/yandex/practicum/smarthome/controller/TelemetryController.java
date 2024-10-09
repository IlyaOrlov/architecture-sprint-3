package ru.yandex.practicum.smarthome.controller;

import ru.yandex.practicum.smarthome.dto.TelemetryDto;
import ru.yandex.practicum.smarthome.dto.TelemetryReqDto;
import ru.yandex.practicum.smarthome.service.TelemetryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/telemetry")
public class TelemetryController {

    @Autowired
    private TelemetryService telemetryService;

    @PostMapping
    public ResponseEntity<TelemetryDto> addTelemetry(@RequestBody TelemetryReqDto req) {
        TelemetryDto telemetry = telemetryService.addTelemetry(req.getDeviceId(), req.getDeviceInfo(), req.getTimestamp());
        return ResponseEntity.status(201).body(telemetry);
    }

    @GetMapping("/device/{device_id}/latest")
    public ResponseEntity<TelemetryDto> getLatestTelemetry(@PathVariable("device_id") Long deviceId) {
        TelemetryDto telemetry = telemetryService.getLatestTelemetry(deviceId);
        return telemetry != null ? ResponseEntity.ok(telemetry) : ResponseEntity.notFound().build();
    }

    @GetMapping("/device/{device_id}/history")
    public ResponseEntity<List<TelemetryDto>> getTelemetryHistory(
            @PathVariable("device_id") Long deviceId,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        List<TelemetryDto> telemetryHistory = telemetryService.getTelemetryHistory(deviceId, start, end);
        return telemetryHistory.isEmpty() ? ResponseEntity.status(404).build() : ResponseEntity.ok(telemetryHistory);
    }
}
