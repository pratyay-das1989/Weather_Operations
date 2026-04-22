package com.pratyay.practice.weatherinfo.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.Serializable;

class WeatherModelTest {

    private WeatherModel weatherModel;

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
    }

    @Test
    void testSerializableInterface() {
        assertTrue(weatherModel instanceof Serializable);
    }

    @Test
    void testBuilder() {
        assertNotNull(weatherModel);
        assertEquals(1, weatherModel.getId());
        assertEquals("2023-12-01", weatherModel.getDate());
        assertEquals(40.7128, weatherModel.getLat());
        assertEquals(-74.0060, weatherModel.getLon());
        assertEquals("New York", weatherModel.getCity());
        assertEquals("NY", weatherModel.getState());
        assertEquals(15.5, weatherModel.getTemperature());
    }

    @Test
    void testNoArgsConstructor() {
        WeatherModel emptyModel = new WeatherModel();
        assertNotNull(emptyModel);
        assertNull(emptyModel.getId());
        assertNull(emptyModel.getDate());
        assertEquals(0.0, emptyModel.getLat());
        assertEquals(0.0, emptyModel.getLon());
        assertNull(emptyModel.getCity());
        assertNull(emptyModel.getState());
        assertEquals(0.0, emptyModel.getTemperature());
    }

    @Test
    void testAllArgsConstructor() {
        WeatherModel fullModel = new WeatherModel(2, "2023-12-02", 41.8781, -87.6298, "Chicago", "IL", 10.2);
        assertNotNull(fullModel);
        assertEquals(2, fullModel.getId());
        assertEquals("2023-12-02", fullModel.getDate());
        assertEquals(41.8781, fullModel.getLat());
        assertEquals(-87.6298, fullModel.getLon());
        assertEquals("Chicago", fullModel.getCity());
        assertEquals("IL", fullModel.getState());
        assertEquals(10.2, fullModel.getTemperature());
    }

    @Test
    void testSettersAndGetters() {
        WeatherModel testModel = new WeatherModel();
        
        testModel.setId(3);
        testModel.setDate("2023-12-03");
        testModel.setLat(34.0522);
        testModel.setLon(-118.2437);
        testModel.setCity("Los Angeles");
        testModel.setState("CA");
        testModel.setTemperature(25.8);

        assertEquals(3, testModel.getId());
        assertEquals("2023-12-03", testModel.getDate());
        assertEquals(34.0522, testModel.getLat());
        assertEquals(-118.2437, testModel.getLon());
        assertEquals("Los Angeles", testModel.getCity());
        assertEquals("CA", testModel.getState());
        assertEquals(25.8, testModel.getTemperature());
    }

    @Test
    void testToBuilder() {
        WeatherModel modifiedModel = weatherModel.toBuilder()
                .city("Boston")
                .state("MA")
                .temperature(5.2)
                .build();

        assertEquals(1, modifiedModel.getId());
        assertEquals("2023-12-01", modifiedModel.getDate());
        assertEquals(40.7128, modifiedModel.getLat());
        assertEquals(-74.0060, modifiedModel.getLon());
        assertEquals("Boston", modifiedModel.getCity());
        assertEquals("MA", modifiedModel.getState());
        assertEquals(5.2, modifiedModel.getTemperature());
    }

    @Test
    void testEqualsAndHashCode() {
        WeatherModel model1 = WeatherModel.builder()
                .id(1)
                .date("2023-12-01")
                .lat(40.7128)
                .lon(-74.0060)
                .city("New York")
                .state("NY")
                .temperature(15.5)
                .build();

        WeatherModel model2 = WeatherModel.builder()
                .id(1)
                .date("2023-12-01")
                .lat(40.7128)
                .lon(-74.0060)
                .city("New York")
                .state("NY")
                .temperature(15.5)
                .build();

        WeatherModel model3 = WeatherModel.builder()
                .id(2)
                .date("2023-12-02")
                .lat(41.8781)
                .lon(-87.6298)
                .city("Chicago")
                .state("IL")
                .temperature(10.2)
                .build();

        assertEquals(model1, model2);
        assertEquals(model1.hashCode(), model2.hashCode());
        assertNotEquals(model1, model3);
        assertNotEquals(model1.hashCode(), model3.hashCode());
    }

    @Test
    void testToString() {
        String modelString = weatherModel.toString();
        assertNotNull(modelString);
        assertTrue(modelString.contains("New York"));
        assertTrue(modelString.contains("NY"));
        assertTrue(modelString.contains("15.5"));
    }

    @Test
    void testNullValues() {
        WeatherModel nullModel = new WeatherModel();
        nullModel.setId(null);
        nullModel.setDate(null);
        nullModel.setCity(null);
        nullModel.setState(null);

        assertNull(nullModel.getId());
        assertNull(nullModel.getDate());
        assertNull(nullModel.getCity());
        assertNull(nullModel.getState());
        assertEquals(0.0, nullModel.getLat());
        assertEquals(0.0, nullModel.getLon());
        assertEquals(0.0, nullModel.getTemperature());
    }

    @Test
    void testNegativeCoordinates() {
        WeatherModel negativeModel = WeatherModel.builder()
                .id(4)
                .date("2023-12-04")
                .lat(-33.8688)
                .lon(151.2093)
                .city("Sydney")
                .state("NSW")
                .temperature(28.3)
                .build();

        assertEquals(-33.8688, negativeModel.getLat());
        assertEquals(151.2093, negativeModel.getLon());
        assertEquals("Sydney", negativeModel.getCity());
        assertEquals("NSW", negativeModel.getState());
        assertEquals(28.3, negativeModel.getTemperature());
    }
}
