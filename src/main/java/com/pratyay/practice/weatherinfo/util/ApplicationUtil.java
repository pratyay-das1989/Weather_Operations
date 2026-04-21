package com.pratyay.practice.weatherinfo.util;

import com.pratyay.practice.weatherinfo.entity.Weather;
import com.pratyay.practice.weatherinfo.model.WeatherModel;
import org.springframework.stereotype.Component;

@Component
public class ApplicationUtil {

    public Weather convertWeatherModelToWeatherEntity(WeatherModel weatherModel) {
       return Weather.builder()
                .date(weatherModel.getDate())
                .lat(weatherModel.getLat())
                .lon(weatherModel.getLon())
                .city(weatherModel.getCity())
                .state(weatherModel.getState())
                .temperature(weatherModel.getTemperature())
                .build();
    }

    public WeatherModel convertWeatherEntityToWeatherModel(Weather weatherEntity) {
        return WeatherModel.builder()
                .id(weatherEntity.getId())
                .date(weatherEntity.getDate())
                .lat(weatherEntity.getLat())
                .lon(weatherEntity.getLon())
                .city(weatherEntity.getCity())
                .state(weatherEntity.getState())
                .temperature(weatherEntity.getTemperature())
                .build();
    }
}
