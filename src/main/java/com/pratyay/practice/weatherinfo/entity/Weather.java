package com.pratyay.practice.weatherinfo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank(message = "Date is required")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date must be in YYYY-MM-DD format")
    @Column(name = "date", nullable = false, length = 10)
    private String date;

    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    @Column(name = "lat", nullable = false)
    private double lat;

    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    @Column(name = "lon", nullable = false)
    private double lon;

    @NotBlank(message = "City is required")
    @Size(min = 1, max = 100, message = "City name must be between 1 and 100 characters")
    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Size(max = 100, message = "State name must not exceed 100 characters")
    @Column(name = "state", length = 100)
    private String state;

    @DecimalMin(value = "-100.0", message = "Temperature must be realistic")
    @DecimalMax(value = "100.0", message = "Temperature must be realistic")
    @Column(name = "temperature", nullable = false)
    private double temperature;
}
