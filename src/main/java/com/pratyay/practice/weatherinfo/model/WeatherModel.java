package com.pratyay.practice.weatherinfo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class WeatherModel implements Serializable {
    private Integer id;
    private String date;
    private double lat;
    private double lon;
    private String city;
    private String state;
    private double temperature;
}
