package ru.yandex.practicum.smarthome.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceCommandReqDto {
    private String command;
    private Map<String, String> parameters;
}
