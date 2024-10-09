package ru.yandex.practicum.smarthome.controller;

import ru.yandex.practicum.smarthome.dto.DeviceDto;
import ru.yandex.practicum.smarthome.dto.DeviceRegistrationReqDto;
import ru.yandex.practicum.smarthome.dto.DeviceStatusUpdateReqDto;
import ru.yandex.practicum.smarthome.dto.DeviceCommandReqDto;
import ru.yandex.practicum.smarthome.dto.DeviceCommandResDto;
import ru.yandex.practicum.smarthome.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping("/{id}")
    public ResponseEntity<DeviceDto> getDevice(@PathVariable Long id) {
        DeviceDto device = deviceService.getDevice(id);
        return device != null ? ResponseEntity.ok(device) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<DeviceDto> createDevice(@RequestBody DeviceRegistrationReqDto req) {
        DeviceDto createdDevice = deviceService.createDevice(req.getUserId(), req.getDeviceType());
        return ResponseEntity.status(201).body(createdDevice);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<DeviceDto> updateDeviceStatus(@PathVariable Long id, @RequestBody DeviceStatusUpdateReqDto req) {
        DeviceDto updatedDevice = deviceService.updateDeviceStatus(id, req.getStatus());
        return updatedDevice != null ? ResponseEntity.ok(updatedDevice) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/commands")
    public ResponseEntity<DeviceCommandResDto> sendCommand(@PathVariable Long id, @RequestBody DeviceCommandReqDto req) {
        DeviceCommandResDto response = deviceService.sendCommand(id, req.getCommand(), req.getParameters());
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }
}
