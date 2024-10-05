package ru.yandex.practicum.smarthome.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceCommandResDto {
    private Long id;
    private String command;
    private String status;
}
