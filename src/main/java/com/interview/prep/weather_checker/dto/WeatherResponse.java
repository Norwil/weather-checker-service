package com.interview.prep.weather_checker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record WeatherResponse(
        @JsonProperty("current_weather") CurrentWeather currentWeather,
        double latitude,
        double longitude
) implements Serializable {
}
