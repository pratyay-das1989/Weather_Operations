package com.pratyay.practice.weatherinfo.controller;

import com.pratyay.practice.weatherinfo.entity.Weather;
import com.pratyay.practice.weatherinfo.model.WeatherModel;
import com.pratyay.practice.weatherinfo.repository.WeatherRepository;
import com.pratyay.practice.weatherinfo.util.ApplicationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class WeatherOperationController {

    private final WeatherRepository weatherRepository;
    private final ApplicationUtil applicationUtil;

    @PostMapping("/weather")
    public ResponseEntity<WeatherModel> weatherOperation(@RequestBody WeatherModel weatherModel) {
        Weather weatherEntity = new Weather();
        try {
            weatherEntity = weatherRepository.save(applicationUtil.convertWeatherModelToWeatherEntity(weatherModel));
            return ResponseEntity.ok().body(applicationUtil.convertWeatherEntityToWeatherModel(weatherEntity));
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/weather")
    public ResponseEntity<List<WeatherModel>> getAllWeatherInfo(){
        return ResponseEntity.ok(weatherRepository.findAll().stream().sorted(Comparator.comparingInt(Weather::getId)).map(applicationUtil::convertWeatherEntityToWeatherModel).toList());
    }

    @GetMapping("/weather/{id}")
    public ResponseEntity<WeatherModel> getWeatherInfo(@PathVariable Integer id){
        if(!weatherRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok().body(applicationUtil.convertWeatherEntityToWeatherModel(weatherRepository.findById(id).get()));
        }
    }

    @DeleteMapping("/weather/{id}")
    public ResponseEntity<WeatherModel> deleteWeatherInfo(@PathVariable Integer id){
        if(!weatherRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.noContent().build();
        }
    }

}
