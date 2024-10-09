package ru.yandex.practicum.smarthome.repository;

import ru.yandex.practicum.smarthome.entity.Telemetry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TelemetryRepository extends JpaRepository<Telemetry, Long> {
    Optional<Telemetry> findFirstByDeviceIdOrderByTimestampDesc(Long deviceId);
    List<Telemetry> findAllByDeviceIdAndTimestampBetween(Long deviceId, LocalDateTime start, LocalDateTime end);
}