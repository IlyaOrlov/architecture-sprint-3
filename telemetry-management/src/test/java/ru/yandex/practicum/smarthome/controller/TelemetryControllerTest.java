package ru.yandex.practicum.smarthome.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import ru.yandex.practicum.smarthome.dto.TelemetryDto;
import ru.yandex.practicum.smarthome.dto.TelemetryReqDto;
import ru.yandex.practicum.smarthome.service.TelemetryService;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class TelemetryControllerTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TelemetryService telemetryService;

    private TelemetryReqDto telemetryReqDto;

    @BeforeEach
    void setUp() {
        telemetryReqDto = new TelemetryReqDto(
            1L, Map.of("status", "ON", "temperature", "23.5"),
            LocalDateTime.parse("2024-09-30T12:45:00"));
    }

    @Test
    public void testAddTelemetry() throws Exception {
        mockMvc.perform(post("/api/telemetry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(telemetryReqDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.device_id").value(1))
                .andExpect(jsonPath("$.timestamp").value("2024-09-30T12:45:00"))
                .andExpect(jsonPath("$.device_info.status").value("ON"))
                .andExpect(jsonPath("$.device_info.temperature").value(23.5));
    }

    @Test
    public void testGetLatestTelemetry() throws Exception {
        TelemetryDto telemetry = telemetryService.addTelemetry(
            telemetryReqDto.getDeviceId(),
            telemetryReqDto.getDeviceInfo(),
            telemetryReqDto.getTimestamp());

        mockMvc.perform(get("/api/telemetry/device/{device_id}/latest", telemetry.getDeviceId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(OK.value()))
                .andExpect(jsonPath("$.device_id").value(telemetry.getDeviceId()))
                .andExpect(jsonPath("$.device_info.status").value("ON"))
                .andExpect(jsonPath("$.device_info.temperature").value(23.5));
    }

    @Test
    public void testGetTelemetryHistory() throws Exception {
        TelemetryDto telemetry = telemetryService.addTelemetry(
            telemetryReqDto.getDeviceId(),
            telemetryReqDto.getDeviceInfo(),
            telemetryReqDto.getTimestamp());

        LocalDate day = telemetryReqDto.getTimestamp().toLocalDate();
        String start = day.atStartOfDay().toString();
        String end = day.atTime(23, 59, 59).toString();

        mockMvc.perform(get("/api/telemetry/device/{device_id}/history", telemetry.getDeviceId())
                .param("start", start)
                .param("end", end)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(OK.value()))
                .andExpect(jsonPath("$[0].device_id").value(telemetry.getDeviceId()))
                .andExpect(jsonPath("$[0].device_info.status").value("ON"))
                .andExpect(jsonPath("$[0].device_info.temperature").value(23.5));
    }
}
