package com.pratyay.practice.weatherinfo.util;

import com.pratyay.practice.weatherinfo.entity.Weather;
import com.pratyay.practice.weatherinfo.model.WeatherModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ApplicationUtilTest {

    private ApplicationUtil applicationUtil;
    private WeatherModel weatherModel;
    private Weather weatherEntity;

    @BeforeEach
    void setUp() {
        applicationUtil = new ApplicationUtil();
        
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
    void testConvertWeatherModelToWeatherEntity() {
        Weather result = applicationUtil.convertWeatherModelToWeatherEntity(weatherModel);

        assertNotNull(result);
        assertEquals(weatherModel.getDate(), result.getDate());
        assertEquals(weatherModel.getLat(), result.getLat());
        assertEquals(weatherModel.getLon(), result.getLon());
        assertEquals(weatherModel.getCity(), result.getCity());
        assertEquals(weatherModel.getState(), result.getState());
        assertEquals(weatherModel.getTemperature(), result.getTemperature());
        assertNull(result.getId());
    }

    @Test
    void testConvertWeatherModelToWeatherEntityWithNullInput() {
        Weather result = applicationUtil.convertWeatherModelToWeatherEntity(null);

        assertNull(result);
    }

    @Test
    void testConvertWeatherModelToWeatherEntityWithNullFields() {
        WeatherModel nullModel = WeatherModel.builder()
                .id(null)
                .date(null)
                .city(null)
                .state(null)
                .build();

        Weather result = applicationUtil.convertWeatherModelToWeatherEntity(nullModel);

        assertNotNull(result);
        assertNull(result.getDate());
        assertNull(result.getCity());
        assertNull(result.getState());
        assertEquals(0.0, result.getLat());
        assertEquals(0.0, result.getLon());
        assertEquals(0.0, result.getTemperature());
    }

    @Test
    void testConvertWeatherEntityToWeatherModel() {
        WeatherModel result = applicationUtil.convertWeatherEntityToWeatherModel(weatherEntity);

        assertNotNull(result);
        assertEquals(weatherEntity.getId(), result.getId());
        assertEquals(weatherEntity.getDate(), result.getDate());
        assertEquals(weatherEntity.getLat(), result.getLat());
        assertEquals(weatherEntity.getLon(), result.getLon());
        assertEquals(weatherEntity.getCity(), result.getCity());
        assertEquals(weatherEntity.getState(), result.getState());
        assertEquals(weatherEntity.getTemperature(), result.getTemperature());
    }

    @Test
    void testConvertWeatherEntityToWeatherModelWithNullInput() {
        WeatherModel result = applicationUtil.convertWeatherEntityToWeatherModel(null);

        assertNull(result);
    }

    @Test
    void testConvertWeatherEntityToWeatherModelWithNullFields() {
        Weather nullEntity = Weather.builder()
                .id(null)
                .date(null)
                .city(null)
                .state(null)
                .build();

        WeatherModel result = applicationUtil.convertWeatherEntityToWeatherModel(nullEntity);

        assertNotNull(result);
        assertNull(result.getId());
        assertNull(result.getDate());
        assertNull(result.getCity());
        assertNull(result.getState());
        assertEquals(0.0, result.getLat());
        assertEquals(0.0, result.getLon());
        assertEquals(0.0, result.getTemperature());
    }

    @Test
    void testConvertWeatherModelToWeatherEntityWithNegativeCoordinates() {
        WeatherModel negativeModel = WeatherModel.builder()
                .id(2)
                .date("2023-12-02")
                .lat(-33.8688)
                .lon(151.2093)
                .city("Sydney")
                .state("NSW")
                .temperature(28.3)
                .build();

        Weather result = applicationUtil.convertWeatherModelToWeatherEntity(negativeModel);

        assertNotNull(result);
        assertEquals(-33.8688, result.getLat());
        assertEquals(151.2093, result.getLon());
        assertEquals("Sydney", result.getCity());
        assertEquals("NSW", result.getState());
        assertEquals(28.3, result.getTemperature());
    }

    @Test
    void testConvertWeatherEntityToWeatherModelWithNegativeCoordinates() {
        Weather negativeEntity = Weather.builder()
                .id(2)
                .date("2023-12-02")
                .lat(-33.8688)
                .lon(151.2093)
                .city("Sydney")
                .state("NSW")
                .temperature(28.3)
                .build();

        WeatherModel result = applicationUtil.convertWeatherEntityToWeatherModel(negativeEntity);

        assertNotNull(result);
        assertEquals(-33.8688, result.getLat());
        assertEquals(151.2093, result.getLon());
        assertEquals("Sydney", result.getCity());
        assertEquals("NSW", result.getState());
        assertEquals(28.3, result.getTemperature());
    }

    @Test
    void testConvertWeatherModelToWeatherEntityWithZeroValues() {
        WeatherModel zeroModel = WeatherModel.builder()
                .id(3)
                .date("2023-12-03")
                .lat(0.0)
                .lon(0.0)
                .city("Equator")
                .state("Prime Meridian")
                .temperature(0.0)
                .build();

        Weather result = applicationUtil.convertWeatherModelToWeatherEntity(zeroModel);

        assertNotNull(result);
        assertEquals(0.0, result.getLat());
        assertEquals(0.0, result.getLon());
        assertEquals("Equator", result.getCity());
        assertEquals("Prime Meridian", result.getState());
        assertEquals(0.0, result.getTemperature());
    }

    @Test
    void testConvertWeatherEntityToWeatherModelWithZeroValues() {
        Weather zeroEntity = Weather.builder()
                .id(3)
                .date("2023-12-03")
                .lat(0.0)
                .lon(0.0)
                .city("Equator")
                .state("Prime Meridian")
                .temperature(0.0)
                .build();

        WeatherModel result = applicationUtil.convertWeatherEntityToWeatherModel(zeroEntity);

        assertNotNull(result);
        assertEquals(0.0, result.getLat());
        assertEquals(0.0, result.getLon());
        assertEquals("Equator", result.getCity());
        assertEquals("Prime Meridian", result.getState());
        assertEquals(0.0, result.getTemperature());
    }

    @Test
    void testBidirectionalConversion() {
        Weather convertedEntity = applicationUtil.convertWeatherModelToWeatherEntity(weatherModel);
        WeatherModel convertedBackModel = applicationUtil.convertWeatherEntityToWeatherModel(convertedEntity);

        assertEquals(weatherModel.getDate(), convertedBackModel.getDate());
        assertEquals(weatherModel.getLat(), convertedBackModel.getLat());
        assertEquals(weatherModel.getLon(), convertedBackModel.getLon());
        assertEquals(weatherModel.getCity(), convertedBackModel.getCity());
        assertEquals(weatherModel.getState(), convertedBackModel.getState());
        assertEquals(weatherModel.getTemperature(), convertedBackModel.getTemperature());
    }

    @Test
    void testComponentAnnotation() {
        assertNotNull(applicationUtil);
        assertTrue(applicationUtil instanceof ApplicationUtil);
    }
}
