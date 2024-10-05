package ru.yandex.practicum.smarthome.service;

import ru.yandex.practicum.smarthome.dto.DeviceDto;
import ru.yandex.practicum.smarthome.dto.DeviceCommandResDto;

import java.util.Map;

public interface DeviceService {
    public DeviceDto getDevice(Long id);
    public DeviceDto createDevice(Long userId, String deviceType);
    public DeviceDto updateDeviceStatus(Long id, String status);
    public DeviceCommandResDto sendCommand(Long id, String command, Map<String, String> parameters);
}
