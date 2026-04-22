package com.pratyay.practice.weatherinfo.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class WeatherTest {

    private Weather weather;

    @BeforeEach
    void setUp() {
        weather = Weather.builder()
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
    void testBuilder() {
        assertNotNull(weather);
        assertEquals(1, weather.getId());
        assertEquals("2023-12-01", weather.getDate());
        assertEquals(40.7128, weather.getLat());
        assertEquals(-74.0060, weather.getLon());
        assertEquals("New York", weather.getCity());
        assertEquals("NY", weather.getState());
        assertEquals(15.5, weather.getTemperature());
    }

    @Test
    void testNoArgsConstructor() {
        Weather emptyWeather = new Weather();
        assertNotNull(emptyWeather);
        assertNull(emptyWeather.getId());
        assertNull(emptyWeather.getDate());
        assertEquals(0.0, emptyWeather.getLat());
        assertEquals(0.0, emptyWeather.getLon());
        assertNull(emptyWeather.getCity());
        assertNull(emptyWeather.getState());
        assertEquals(0.0, emptyWeather.getTemperature());
    }

    @Test
    void testAllArgsConstructor() {
        Weather fullWeather = new Weather(2, "2023-12-02", 41.8781, -87.6298, "Chicago", "IL", 10.2);
        assertNotNull(fullWeather);
        assertEquals(2, fullWeather.getId());
        assertEquals("2023-12-02", fullWeather.getDate());
        assertEquals(41.8781, fullWeather.getLat());
        assertEquals(-87.6298, fullWeather.getLon());
        assertEquals("Chicago", fullWeather.getCity());
        assertEquals("IL", fullWeather.getState());
        assertEquals(10.2, fullWeather.getTemperature());
    }

    @Test
    void testSettersAndGetters() {
        Weather testWeather = new Weather();
        
        testWeather.setId(3);
        testWeather.setDate("2023-12-03");
        testWeather.setLat(34.0522);
        testWeather.setLon(-118.2437);
        testWeather.setCity("Los Angeles");
        testWeather.setState("CA");
        testWeather.setTemperature(25.8);

        assertEquals(3, testWeather.getId());
        assertEquals("2023-12-03", testWeather.getDate());
        assertEquals(34.0522, testWeather.getLat());
        assertEquals(-118.2437, testWeather.getLon());
        assertEquals("Los Angeles", testWeather.getCity());
        assertEquals("CA", testWeather.getState());
        assertEquals(25.8, testWeather.getTemperature());
    }

    @Test
    void testToBuilder() {
        Weather modifiedWeather = weather.toBuilder()
                .city("Boston")
                .state("MA")
                .temperature(5.2)
                .build();

        assertEquals(1, modifiedWeather.getId());
        assertEquals("2023-12-01", modifiedWeather.getDate());
        assertEquals(40.7128, modifiedWeather.getLat());
        assertEquals(-74.0060, modifiedWeather.getLon());
        assertEquals("Boston", modifiedWeather.getCity());
        assertEquals("MA", modifiedWeather.getState());
        assertEquals(5.2, modifiedWeather.getTemperature());
    }

    @Test
    void testEqualsAndHashCode() {
        Weather weather1 = Weather.builder()
                .id(1)
                .date("2023-12-01")
                .lat(40.7128)
                .lon(-74.0060)
                .city("New York")
                .state("NY")
                .temperature(15.5)
                .build();

        Weather weather2 = Weather.builder()
                .id(1)
                .date("2023-12-01")
                .lat(40.7128)
                .lon(-74.0060)
                .city("New York")
                .state("NY")
                .temperature(15.5)
                .build();

        Weather weather3 = Weather.builder()
                .id(2)
                .date("2023-12-02")
                .lat(41.8781)
                .lon(-87.6298)
                .city("Chicago")
                .state("IL")
                .temperature(10.2)
                .build();

        assertEquals(weather1, weather2);
        assertEquals(weather1.hashCode(), weather2.hashCode());
        assertNotEquals(weather1, weather3);
        assertNotEquals(weather1.hashCode(), weather3.hashCode());
    }

    @Test
    void testToString() {
        String weatherString = weather.toString();
        assertNotNull(weatherString);
        assertTrue(weatherString.contains("New York"));
        assertTrue(weatherString.contains("NY"));
        assertTrue(weatherString.contains("15.5"));
    }
}
