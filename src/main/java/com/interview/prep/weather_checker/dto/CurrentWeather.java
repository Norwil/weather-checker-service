package com.interview.prep.weather_checker.dto;

import java.io.Serializable;

public record CurrentWeather(
        double temperature,
        double windspeed,
        int weathercode
        ) implements Serializable {
}
