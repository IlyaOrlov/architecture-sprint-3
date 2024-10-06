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

import ru.yandex.practicum.smarthome.dto.DeviceDto;
import ru.yandex.practicum.smarthome.dto.DeviceRegistrationReqDto;
import ru.yandex.practicum.smarthome.service.DeviceService;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class DeviceControllerTest {

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
    private DeviceService deviceService;

    private DeviceRegistrationReqDto regDeviceDto;

    @BeforeEach
    void setUp() {
        regDeviceDto = new DeviceRegistrationReqDto(1L, "heating");
    }

    @Test
    void registerDevice_ShouldReturnCreatedDevice() throws Exception {
        mockMvc.perform(post("/api/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(regDeviceDto)))
                .andExpect(status().is(CREATED.value()))
                .andExpect(jsonPath("$.user_id").value(1))
                .andExpect(jsonPath("$.device_type").value("heating"))
                .andExpect(jsonPath("$.status").value("OFF"));
    }

    @Test
    void getDevice_ShouldReturnDevice() throws Exception {
        DeviceDto device = deviceService.createDevice(regDeviceDto.getUserId(),regDeviceDto.getDeviceType());

        mockMvc.perform(get("/api/devices/{id}", device.getId()))
                .andExpect(status().is(OK.value()))
                .andExpect(jsonPath("$.id").value(device.getId()))
                .andExpect(jsonPath("$.user_id").value(device.getUserId()))
                .andExpect(jsonPath("$.device_type").value(device.getDeviceType()))
                .andExpect(jsonPath("$.status").value(device.getStatus()));
    }

    @Test
    void getDevice_ShouldReturnNotFound_WhenDeviceDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/devices/{id}", 999L))
                .andExpect(status().is(NOT_FOUND.value()));
    }

    @Test
    void updateDeviceStatus_ShouldReturnUpdatedDevice() throws Exception {
        DeviceDto device = deviceService.createDevice(regDeviceDto.getUserId(), regDeviceDto.getDeviceType());

        mockMvc.perform(put("/api/devices/{id}/status", device.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\": \"ON\"}"))
                .andExpect(status().is(OK.value()))
                .andExpect(jsonPath("$.id").value(device.getId()))
                .andExpect(jsonPath("$.status").value("ON"));
    }

    @Test
    void updateDeviceStatus_ShouldReturnNotFound_WhenDeviceDoesNotExist() throws Exception {
        mockMvc.perform(put("/api/devices/{id}/status", 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\": \"ON\"}"))
                .andExpect(status().is(NOT_FOUND.value()));
    }

    @Test
    void sendCommand_ShouldReturnSuccessStatus() throws Exception {
        DeviceDto device = deviceService.createDevice(regDeviceDto.getUserId(), regDeviceDto.getDeviceType());
        
        mockMvc.perform(post("/api/devices/{id}/commands", device.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"command\": \"set-temperature\", \"parameters\": {\"temperature\": \"22.5\"} }"))
                .andExpect(status().is(OK.value()))
                .andExpect(content().string(containsString("success")));
    }

    @Test
    void sendCommand_ShouldReturnNotFound_WhenDeviceDoesNotExist() throws Exception {
        mockMvc.perform(post("/api/devices/{id}/commands", 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"command\": \"set-temperature\", \"parameters\": {\"temperature\": \"22.5\"} }"))
                .andExpect(status().is(NOT_FOUND.value()));
    }
}
