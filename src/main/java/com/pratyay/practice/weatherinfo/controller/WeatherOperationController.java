package com.pratyay.practice.weatherinfo.controller;

import com.pratyay.practice.weatherinfo.entity.Weather;
import com.pratyay.practice.weatherinfo.model.WeatherModel;
import com.pratyay.practice.weatherinfo.repository.WeatherRepository;
import com.pratyay.practice.weatherinfo.util.ApplicationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1")
public class WeatherOperationController {

    private final WeatherRepository weatherRepository;
    private final ApplicationUtil applicationUtil;

    @PostMapping("/weather")
    public ResponseEntity<WeatherModel> createWeather(@Valid @RequestBody WeatherModel weatherModel) {
        try {
            log.info("Creating weather record for city: {}", weatherModel.getCity());
            Weather weatherEntity = weatherRepository.save(applicationUtil.convertWeatherModelToWeatherEntity(weatherModel));
            WeatherModel response = applicationUtil.convertWeatherEntityToWeatherModel(weatherEntity);
            log.info("Successfully created weather record with ID: {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DataAccessException e) {
            log.error("Database error while creating weather record: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            log.error("Unexpected error while creating weather record: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/weather")
    public ResponseEntity<List<WeatherModel>> getAllWeatherInfo(){
        try {
            List<WeatherModel> weatherList = weatherRepository.findAll().stream()
                    .sorted(Comparator.comparingInt(Weather::getId))
                    .map(applicationUtil::convertWeatherEntityToWeatherModel)
                    .toList();
            log.info("Retrieved {} weather records", weatherList.size());
            return ResponseEntity.ok(weatherList);
        } catch (Exception e) {
            log.error("Error retrieving weather records: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/weather/{id}")
    public ResponseEntity<WeatherModel> getWeatherInfo(@PathVariable Integer id){
        try {
            if (id == null || id <= 0) {
                log.warn("Invalid ID provided: {}", id);
                return ResponseEntity.badRequest().build();
            }

            if (!weatherRepository.existsById(id)) {
                log.warn("Weather record not found for ID: {}", id);
                return ResponseEntity.notFound().build();
            }

            Optional<Weather> weatherEntity = weatherRepository.findById(id);
            if (weatherEntity.isEmpty()) {
                log.warn("Weather record not found for ID: {}", id);
                return ResponseEntity.notFound().build();
            }

            WeatherModel response = applicationUtil.convertWeatherEntityToWeatherModel(weatherEntity.get());
            log.info("Retrieved weather record for ID: {}", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving weather record for ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/weather/{id}")
    public ResponseEntity<Void> deleteWeatherInfo(@PathVariable Integer id){
        try {
            if (id == null || id <= 0) {
                log.warn("Invalid ID provided for deletion: {}", id);
                return ResponseEntity.badRequest().build();
            }

            if (!weatherRepository.existsById(id)) {
                log.warn("Weather record not found for deletion with ID: {}", id);
                return ResponseEntity.notFound().build();
            }

            weatherRepository.deleteById(id);
            log.info("Successfully deleted weather record with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting weather record for ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
