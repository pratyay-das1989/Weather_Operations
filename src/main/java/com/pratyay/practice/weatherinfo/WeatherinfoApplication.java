package com.pratyay.practice.weatherinfo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
	info = @Info(
		title = "Weather Information API",
		version = "1.0",
		description = "API for managing weather information data",
		contact = @Contact(
			name = "Weather API Team",
			email = "weather@example.com"
		),
		license = @License(
			name = "Apache 2.0",
			url = "https://www.apache.org/licenses/LICENSE-2.0"
		)
	)
)
public class WeatherinfoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherinfoApplication.class, args);
	}

}
