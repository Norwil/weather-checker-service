package com.interview.prep.weather_checker.controller;

import com.interview.prep.weather_checker.dto.WeatherResponse;
import com.interview.prep.weather_checker.service.WeatherService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
@Slf4j
@Validated
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("{city}")
    public WeatherResponse getWeather(@PathVariable("city") @NotNull String city) {
        log.info("Received request to check weather for city: {}", city);

        return weatherService.getWeather(city);
    }
}
