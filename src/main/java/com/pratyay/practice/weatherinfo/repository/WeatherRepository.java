package com.pratyay.practice.weatherinfo.repository;

import com.pratyay.practice.weatherinfo.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Integer> {
}
