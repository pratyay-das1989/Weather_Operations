package com.pratyay.practice.weatherinfo.util;

import com.pratyay.practice.weatherinfo.entity.Weather;
import com.pratyay.practice.weatherinfo.model.WeatherModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplicationUtil {

    public Weather convertWeatherModelToWeatherEntity(WeatherModel weatherModel) {
        if (weatherModel == null) {
            log.warn("WeatherModel is null, returning null Weather entity");
            return null;
        }
        
        try {
            Weather weatherEntity = Weather.builder()
                    .date(weatherModel.getDate())
                    .lat(weatherModel.getLat())
                    .lon(weatherModel.getLon())
                    .city(weatherModel.getCity())
                    .state(weatherModel.getState())
                    .temperature(weatherModel.getTemperature())
                    .build();
            
            log.debug("Successfully converted WeatherModel to Weather entity for city: {}", weatherModel.getCity());
            return weatherEntity;
        } catch (Exception e) {
            log.error("Error converting WeatherModel to Weather entity: {}", e.getMessage());
            throw new IllegalArgumentException("Failed to convert WeatherModel to Weather entity", e);
        }
    }

    public WeatherModel convertWeatherEntityToWeatherModel(Weather weatherEntity) {
        if (weatherEntity == null) {
            log.warn("Weather entity is null, returning null WeatherModel");
            return null;
        }
        
        try {
            WeatherModel weatherModel = WeatherModel.builder()
                    .id(weatherEntity.getId())
                    .date(weatherEntity.getDate())
                    .lat(weatherEntity.getLat())
                    .lon(weatherEntity.getLon())
                    .city(weatherEntity.getCity())
                    .state(weatherEntity.getState())
                    .temperature(weatherEntity.getTemperature())
                    .build();
            
            log.debug("Successfully converted Weather entity to WeatherModel for city: {}", weatherEntity.getCity());
            return weatherModel;
        } catch (Exception e) {
            log.error("Error converting Weather entity to WeatherModel: {}", e.getMessage());
            throw new IllegalArgumentException("Failed to convert Weather entity to WeatherModel", e);
        }
    }
}
