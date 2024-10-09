package ru.yandex.practicum.smarthome.service;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.smarthome.dto.DeviceDto;
import ru.yandex.practicum.smarthome.dto.DeviceCommandResDto;
import ru.yandex.practicum.smarthome.entity.Device;
import ru.yandex.practicum.smarthome.repository.DeviceRepository;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    @Override
    public DeviceDto getDevice(Long id) {
        Optional<Device> device = deviceRepository.findById(id);
        if (device.isPresent()) {
            return convertToDto(device.get());
        }
        return null;
    }

    @Override
    public DeviceDto createDevice(Long userId, String deviceType) {
        Device device = new Device();
        device.setUserId(userId);
        device.setDeviceType(deviceType);
        device.setStatus("OFF");

        device = deviceRepository.save(device);
        return convertToDto(device);
    }

    @Override
    public DeviceDto updateDeviceStatus(Long id, String status) {
        Optional<Device> device = deviceRepository.findById(id);
        if (device.isPresent()) {
            Device updatedDevice = device.get();
            updatedDevice.setStatus(status);
            deviceRepository.save(updatedDevice);
            return convertToDto(updatedDevice);
        }
        return null;
    }

    @Override
    public DeviceCommandResDto sendCommand(Long id, String command, Map<String, String> parameters) {
        Optional<Device> device = deviceRepository.findById(id);
        if (device.isPresent()) {
            // Логика отправки команды устройству
            return new DeviceCommandResDto(id, command, "Command executed successfully");
        }
        return null;
    }

    private DeviceDto convertToDto(Device device) {
        DeviceDto dto = new DeviceDto();
        dto.setId(device.getId());
        dto.setUserId(device.getUserId());
        dto.setStatus(device.getStatus());
        dto.setDeviceType(device.getDeviceType());
        return dto;
    }
}
