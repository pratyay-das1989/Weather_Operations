package com.pratyay.practice.weatherinfo.controller;

import com.pratyay.practice.weatherinfo.entity.Weather;
import com.pratyay.practice.weatherinfo.model.WeatherModel;
import com.pratyay.practice.weatherinfo.repository.WeatherRepository;
import com.pratyay.practice.weatherinfo.util.ApplicationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Weather Operations", description = "APIs for managing weather information")
public class WeatherOperationController {

    private final WeatherRepository weatherRepository;
    private final ApplicationUtil applicationUtil;

    @PostMapping("/weather")
    @Operation(summary = "Create a new weather record", description = "Add a new weather information record to the database")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Weather record created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<WeatherModel> createWeather(
        @Parameter(description = "Weather information to create", required = true)
        @Valid @RequestBody WeatherModel weatherModel) {
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
    @Operation(summary = "Get all weather records", description = "Retrieve all weather information from the database")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved weather records"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
    @Operation(summary = "Get weather by ID", description = "Retrieve a specific weather record by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved weather record"),
        @ApiResponse(responseCode = "400", description = "Invalid ID provided"),
        @ApiResponse(responseCode = "404", description = "Weather record not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<WeatherModel> getWeatherInfo(
        @Parameter(description = "ID of the weather record to retrieve", required = true)
        @PathVariable Integer id){
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
    @Operation(summary = "Delete weather by ID", description = "Delete a specific weather record by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Weather record deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid ID provided"),
        @ApiResponse(responseCode = "404", description = "Weather record not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteWeatherInfo(
        @Parameter(description = "ID of the weather record to delete", required = true)
        @PathVariable Integer id){
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
