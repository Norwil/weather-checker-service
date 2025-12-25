package com.interview.prep.weather_checker.dto;

import java.util.List;

public record GeocodingResponse(List<Location> results) {
}
