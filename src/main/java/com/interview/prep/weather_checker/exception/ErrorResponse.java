package com.interview.prep.weather_checker.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message,
        int statusCode,
        LocalDateTime timestamp
) {
}