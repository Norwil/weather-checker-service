package com.interview.prep.weather_checker.dto;

public record Location(
        String name,
        double latitude,
        double longitude
) {
}