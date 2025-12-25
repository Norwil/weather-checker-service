package com.interview.prep.weather_checker.service;

import com.interview.prep.weather_checker.client.WeatherClient;
import com.interview.prep.weather_checker.dto.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@Slf4j
public class WeatherService {

    private final WeatherClient weatherClient;

    public WeatherService(WeatherClient weatherClient) {
        this.weatherClient = weatherClient;
    }

    @Cacheable(value = "weather", key = "#city.trim().toLowerCase()")
    public WeatherResponse getWeather(String city) {

        String normalizedCity = normalizeCity(city);

        log.info("Fetching weather from External API for: {}", normalizedCity);

        return weatherClient.getCurrentWeather(normalizedCity);
    }

    private String normalizeCity(String city) {
        return city
                .trim()
                .toLowerCase(Locale.ROOT);
    }
}
