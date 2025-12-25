package com.interview.prep.weather_checker;

import com.interview.prep.weather_checker.client.WeatherClient;
import com.interview.prep.weather_checker.dto.CurrentWeather;
import com.interview.prep.weather_checker.dto.WeatherResponse;
import com.interview.prep.weather_checker.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {

    @Mock
    private WeatherClient weatherClient;

    @InjectMocks
    private WeatherService weatherService;

    @Test
    void successTest() {

        // Given
        String city = "berlin";
        CurrentWeather currentWeather = new CurrentWeather(
                -5.3,
                12.8,
                0
        );
        WeatherResponse response = new WeatherResponse(currentWeather);

        when(weatherClient.getCurrentWeather(city)).thenReturn(response);

        // Act
        WeatherResponse actualResponse = weatherService.getWeather(city);

        // Assert
        assertEquals(-5.3, actualResponse.currentWeather().temperature());
        assertEquals(12.8, actualResponse.currentWeather().windspeed());
        assertEquals(0, actualResponse.currentWeather().weathercode());

    }

}
