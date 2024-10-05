package ru.yandex.practicum.smarthome.entity;

import org.hibernate.annotations.Type;

import io.hypersistence.utils.hibernate.type.json.JsonType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "telemetry")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Telemetry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false)
    private Long deviceId;

    @Type(JsonType.class)
    @Column(name = "device_info", columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> deviceInfo;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
}
