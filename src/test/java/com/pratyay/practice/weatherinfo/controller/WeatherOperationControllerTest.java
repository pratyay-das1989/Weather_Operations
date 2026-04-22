package com.pratyay.practice.weatherinfo.controller;

import com.pratyay.practice.weatherinfo.entity.Weather;
import com.pratyay.practice.weatherinfo.model.WeatherModel;
import com.pratyay.practice.weatherinfo.repository.WeatherRepository;
import com.pratyay.practice.weatherinfo.util.ApplicationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherOperationController.class)
@ExtendWith(MockitoExtension.class)
class WeatherOperationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherRepository weatherRepository;

    @MockBean
    private ApplicationUtil applicationUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private WeatherModel weatherModel;
    private Weather weatherEntity;
    private Weather savedWeatherEntity;

    @BeforeEach
    void setUp() {
        weatherModel = WeatherModel.builder()
                .id(1)
                .date("2023-12-01")
                .lat(40.7128)
                .lon(-74.0060)
                .city("New York")
                .state("NY")
                .temperature(15.5)
                .build();

        weatherEntity = Weather.builder()
                .date("2023-12-01")
                .lat(40.7128)
                .lon(-74.0060)
                .city("New York")
                .state("NY")
                .temperature(15.5)
                .build();

        savedWeatherEntity = Weather.builder()
                .id(1)
                .date("2023-12-01")
                .lat(40.7128)
                .lon(-74.0060)
                .city("New York")
                .state("NY")
                .temperature(15.5)
                .build();
    }

    @Test
    void testWeatherOperationSuccess() throws Exception {
        when(applicationUtil.convertWeatherModelToWeatherEntity(any(WeatherModel.class)))
                .thenReturn(weatherEntity);
        when(weatherRepository.save(any(Weather.class)))
                .thenReturn(savedWeatherEntity);
        when(applicationUtil.convertWeatherEntityToWeatherModel(any(Weather.class)))
                .thenReturn(weatherModel);

        mockMvc.perform(post("/api/v1/weather")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(weatherModel)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.date").value("2023-12-01"))
                .andExpect(jsonPath("$.city").value("New York"))
                .andExpect(jsonPath("$.state").value("NY"))
                .andExpect(jsonPath("$.temperature").value(15.5));

        verify(applicationUtil).convertWeatherModelToWeatherEntity(any(WeatherModel.class));
        verify(weatherRepository).save(any(Weather.class));
        verify(applicationUtil).convertWeatherEntityToWeatherModel(any(Weather.class));
    }

    @Test
    void testWeatherOperationException() throws Exception {
        when(applicationUtil.convertWeatherModelToWeatherEntity(any(WeatherModel.class)))
                .thenReturn(weatherEntity);
        when(weatherRepository.save(any(Weather.class)))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/api/v1/weather")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(weatherModel)))
                .andExpect(status().isBadRequest());

        verify(applicationUtil).convertWeatherModelToWeatherEntity(any(WeatherModel.class));
        verify(weatherRepository).save(any(Weather.class));
        verify(applicationUtil, never()).convertWeatherEntityToWeatherModel(any(Weather.class));
    }

    @Test
    void testWeatherOperationWithNullInput() throws Exception {
        mockMvc.perform(post("/api/v1/weather")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"));
    }

    @Test
    void testGetAllWeatherInfoSuccess() throws Exception {
        Weather weather2 = Weather.builder()
                .id(2)
                .date("2023-12-02")
                .lat(41.8781)
                .lon(-87.6298)
                .city("Chicago")
                .state("IL")
                .temperature(10.2)
                .build();

        WeatherModel weatherModel2 = WeatherModel.builder()
                .id(2)
                .date("2023-12-02")
                .lat(41.8781)
                .lon(-87.6298)
                .city("Chicago")
                .state("IL")
                .temperature(10.2)
                .build();

        List<Weather> weatherList = Arrays.asList(savedWeatherEntity, weather2);

        when(weatherRepository.findAll()).thenReturn(weatherList);
        when(applicationUtil.convertWeatherEntityToWeatherModel(savedWeatherEntity))
                .thenReturn(weatherModel);
        when(applicationUtil.convertWeatherEntityToWeatherModel(weather2))
                .thenReturn(weatherModel2);

        mockMvc.perform(get("/api/v1/weather"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].city").value("New York"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].city").value("Chicago"));

        verify(weatherRepository).findAll();
        verify(applicationUtil, times(2)).convertWeatherEntityToWeatherModel(any(Weather.class));
    }

    @Test
    void testGetAllWeatherInfoEmpty() throws Exception {
        when(weatherRepository.findAll()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/v1/weather"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(weatherRepository).findAll();
        verify(applicationUtil, never()).convertWeatherEntityToWeatherModel(any(Weather.class));
    }

    @Test
    void testGetWeatherInfoSuccess() throws Exception {
        when(weatherRepository.existsById(1)).thenReturn(true);
        when(weatherRepository.findById(1)).thenReturn(Optional.of(savedWeatherEntity));
        when(applicationUtil.convertWeatherEntityToWeatherModel(savedWeatherEntity))
                .thenReturn(weatherModel);

        mockMvc.perform(get("/api/v1/weather/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.city").value("New York"))
                .andExpect(jsonPath("$.state").value("NY"));

        verify(weatherRepository).existsById(1);
        verify(weatherRepository).findById(1);
        verify(applicationUtil).convertWeatherEntityToWeatherModel(savedWeatherEntity);
    }

    @Test
    void testGetWeatherInfoNotFound() throws Exception {
        when(weatherRepository.existsById(999)).thenReturn(false);

        mockMvc.perform(get("/api/v1/weather/999"))
                .andExpect(status().isNotFound());

        verify(weatherRepository).existsById(999);
        verify(weatherRepository, never()).findById(999);
        verify(applicationUtil, never()).convertWeatherEntityToWeatherModel(any(Weather.class));
    }

    @Test
    void testDeleteWeatherInfoSuccess() throws Exception {
        when(weatherRepository.existsById(1)).thenReturn(true);
        doNothing().when(weatherRepository).deleteById(1);

        mockMvc.perform(delete("/api/v1/weather/1"))
                .andExpect(status().isNoContent());

        verify(weatherRepository).existsById(1);
        verify(weatherRepository).deleteById(1);
    }

    @Test
    void testDeleteWeatherInfoNotFound() throws Exception {
        when(weatherRepository.existsById(999)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/weather/999"))
                .andExpect(status().isNotFound());

        verify(weatherRepository).existsById(999);
        verify(weatherRepository, never()).deleteById(999);
    }

    @Test
    void testWeatherOperationWithInvalidData() throws Exception {
        WeatherModel invalidModel = new WeatherModel();
        // Empty model will fail validation due to required fields

        mockMvc.perform(post("/api/v1/weather")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidModel)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    void testGetWeatherInfoWithNegativeId() throws Exception {
        // Negative ID validation happens before any repository calls
        mockMvc.perform(get("/api/v1/weather/-1"))
                .andExpect(status().isBadRequest());

        verify(weatherRepository, never()).existsById(anyInt());
        verify(weatherRepository, never()).findById(anyInt());
    }

    @Test
    void testDeleteWeatherInfoWithNegativeId() throws Exception {
        // Negative ID validation happens before any repository calls
        mockMvc.perform(delete("/api/v1/weather/-1"))
                .andExpect(status().isBadRequest());

        verify(weatherRepository, never()).existsById(anyInt());
        verify(weatherRepository, never()).deleteById(anyInt());
    }

    @Test
    void testWeatherOperationWithEmptyJson() throws Exception {
        // Empty JSON object will fail validation due to required fields
        mockMvc.perform(post("/api/v1/weather")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }
}
