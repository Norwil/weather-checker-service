package com.interview.prep.weather_checker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WeatherCheckerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherCheckerApplication.class, args);
	}

}
