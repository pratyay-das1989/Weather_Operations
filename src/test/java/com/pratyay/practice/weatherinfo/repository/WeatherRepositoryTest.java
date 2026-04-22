package com.pratyay.practice.weatherinfo.repository;

import com.pratyay.practice.weatherinfo.entity.Weather;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class WeatherRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private WeatherRepository weatherRepository;

    private Weather weather1;
    private Weather weather2;

    @BeforeEach
    void setUp() {
        weather1 = Weather.builder()
                .date("2023-12-01")
                .lat(40.7128)
                .lon(-74.0060)
                .city("New York")
                .state("NY")
                .temperature(15.5)
                .build();

        weather2 = Weather.builder()
                .date("2023-12-02")
                .lat(41.8781)
                .lon(-87.6298)
                .city("Chicago")
                .state("IL")
                .temperature(10.2)
                .build();
    }

    @Test
    void testSaveWeather() {
        Weather savedWeather = weatherRepository.save(weather1);
        
        assertNotNull(savedWeather);
        assertNotNull(savedWeather.getId());
        assertEquals("2023-12-01", savedWeather.getDate());
        assertEquals(40.7128, savedWeather.getLat());
        assertEquals(-74.0060, savedWeather.getLon());
        assertEquals("New York", savedWeather.getCity());
        assertEquals("NY", savedWeather.getState());
        assertEquals(15.5, savedWeather.getTemperature());
    }

    @Test
    void testFindById() {
        Weather savedWeather = entityManager.persistAndFlush(weather1);
        
        Optional<Weather> foundWeather = weatherRepository.findById(savedWeather.getId());
        
        assertTrue(foundWeather.isPresent());
        assertEquals(savedWeather.getId(), foundWeather.get().getId());
        assertEquals("New York", foundWeather.get().getCity());
        assertEquals("NY", foundWeather.get().getState());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Weather> foundWeather = weatherRepository.findById(999);
        
        assertFalse(foundWeather.isPresent());
    }

    @Test
    void testExistsById() {
        Weather savedWeather = entityManager.persistAndFlush(weather1);
        
        assertTrue(weatherRepository.existsById(savedWeather.getId()));
        assertFalse(weatherRepository.existsById(999));
    }

    @Test
    void testFindAll() {
        entityManager.persistAndFlush(weather1);
        entityManager.persistAndFlush(weather2);
        
        List<Weather> allWeather = weatherRepository.findAll();
        
        assertEquals(2, allWeather.size());
        assertTrue(allWeather.stream().anyMatch(w -> "New York".equals(w.getCity())));
        assertTrue(allWeather.stream().anyMatch(w -> "Chicago".equals(w.getCity())));
    }

    @Test
    void testFindAllEmpty() {
        List<Weather> allWeather = weatherRepository.findAll();
        
        assertTrue(allWeather.isEmpty());
    }

    @Test
    void testCount() {
        entityManager.persistAndFlush(weather1);
        entityManager.persistAndFlush(weather2);
        
        long count = weatherRepository.count();
        
        assertEquals(2, count);
    }

    @Test
    void testCountEmpty() {
        long count = weatherRepository.count();
        
        assertEquals(0, count);
    }

    @Test
    void testDeleteById() {
        Weather savedWeather = entityManager.persistAndFlush(weather1);
        
        assertTrue(weatherRepository.existsById(savedWeather.getId()));
        
        weatherRepository.deleteById(savedWeather.getId());
        
        assertFalse(weatherRepository.existsById(savedWeather.getId()));
    }

    @Test
    void testDeleteByIdNotFound() {
        assertDoesNotThrow(() -> weatherRepository.deleteById(999));
    }

    @Test
    void testDelete() {
        Weather savedWeather = entityManager.persistAndFlush(weather1);
        
        assertTrue(weatherRepository.existsById(savedWeather.getId()));
        
        weatherRepository.delete(savedWeather);
        
        assertFalse(weatherRepository.existsById(savedWeather.getId()));
    }

    @Test
    void testDeleteAll() {
        entityManager.persistAndFlush(weather1);
        entityManager.persistAndFlush(weather2);
        
        assertEquals(2, weatherRepository.count());
        
        weatherRepository.deleteAll();
        
        assertEquals(0, weatherRepository.count());
    }

    @Test
    void testSaveAll() {
        List<Weather> weatherList = List.of(weather1, weather2);
        
        List<Weather> savedWeatherList = weatherRepository.saveAll(weatherList);
        
        assertEquals(2, savedWeatherList.size());
        assertNotNull(savedWeatherList.get(0).getId());
        assertNotNull(savedWeatherList.get(1).getId());
        assertEquals(2, weatherRepository.count());
    }

    @Test
    void testSaveAndUpdate() {
        Weather savedWeather = entityManager.persistAndFlush(weather1);
        
        savedWeather.setTemperature(20.0);
        savedWeather.setCity("Updated City");
        
        Weather updatedWeather = weatherRepository.save(savedWeather);
        
        assertEquals(savedWeather.getId(), updatedWeather.getId());
        assertEquals(20.0, updatedWeather.getTemperature());
        assertEquals("Updated City", updatedWeather.getCity());
    }

    @Test
    void testRepositoryAnnotation() {
        assertNotNull(weatherRepository);
        assertTrue(weatherRepository instanceof org.springframework.data.repository.Repository);
    }
}
