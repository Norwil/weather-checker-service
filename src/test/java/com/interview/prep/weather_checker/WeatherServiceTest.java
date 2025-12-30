package com.interview.prep.weather_checker;

import com.interview.prep.weather_checker.client.WeatherClient;
import com.interview.prep.weather_checker.dto.CurrentWeather;
import com.interview.prep.weather_checker.dto.WeatherResponse;
import com.interview.prep.weather_checker.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Weather Service Unit Tests")
public class WeatherServiceTest {

    @Mock
    private WeatherClient weatherClient;

    @InjectMocks
    private WeatherService weatherService;

    @Captor
    private ArgumentCaptor<String> cityCaptor;

    private WeatherResponse mockResponse;

    @BeforeEach
    void setUp() {
        CurrentWeather current = new CurrentWeather(20.5, 15.0, 1);
        mockResponse = new WeatherResponse(current, 52.52, 13.40);
    }

    @Nested
    @DisplayName("Happy Path Scenarios")
    class HappyPathTests {

        @Test
        @DisplayName("Should return weather data when city name is simple and valid")
        void getWeather_SimpleCity_Success() {
            // Given
            String city = "berlin";
            when(weatherClient.getCurrentWeather("berlin")).thenReturn(mockResponse);

            // When
            WeatherResponse actual = weatherService.getWeather(city);

            // Then
            assertAll("Verify response fields",
                    () -> assertNotNull(actual),
                    () -> assertEquals(20.5, actual.currentWeather().temperature()),
                    () -> assertEquals(52.52, actual.latitude())
            );
        }

        @Test
        @DisplayName("Should normalize city (trim & lowercase) before calling client")
        void getWeather_MixedCaseAndSpaces_ShouldNormalize() {
            // Given
            String dirtyInput = "    LonDON    ";
            String expectedNormalized = "london";

            when(weatherClient.getCurrentWeather(expectedNormalized)).thenReturn(mockResponse);

            // When
            WeatherResponse result = weatherService.getWeather(dirtyInput);

            // Then
            assertNotNull(result);

            // Advanced Verification - To check actual passed normalized string
            verify(weatherClient).getCurrentWeather(cityCaptor.capture());
            assertEquals(expectedNormalized, cityCaptor.getValue(), "Service passed non-normalized city to client");
        }
    }

    @Nested
    @DisplayName("Exception Handling Scenarios")
    class ExceptionTests {

        @Test
        @DisplayName("Should throw NullPointerException when input city is null")
        void getWeather_NullCity_ThrowNPE() {

            // We don't need to mock anything because the code fails before reaching the client.
            assertThrows(NullPointerException.class, () -> weatherService.getWeather(null));
        }

        @Test
        @DisplayName("Should propagate IllegalArgumentException when Client throws it (e.g., City Not Found)")
        void getWeather_CityNotFound_PropagatesException() {
            // Given
            String city = "Atlantis";
            String normalized = "atlantis";
            when(weatherClient.getCurrentWeather(normalized))
                    .thenThrow(new IllegalArgumentException("City not found"));

            // When & Then
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> weatherService.getWeather(city));

            assertEquals("City not found", ex.getMessage());
        }

        @Test
        @DisplayName("Should propagate RunTimeException when Client fails unexpectedly")
        void getWeather_ClientFailure_PropagatesException() {
            // Given
            when(weatherClient.getCurrentWeather(anyString()))
                    .thenThrow(new RuntimeException("API Down"));

            // When & Then
            assertThrows(RuntimeException.class, () -> weatherService.getWeather("berlin"));
        }
    }

    @Nested
    @DisplayName("Edge Cases & Behaviour")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should call client exactly once")
        void getWeather_VerifyInteractionCount() {
            // Given
            when(weatherClient.getCurrentWeather(anyString())).thenReturn(mockResponse);

            // When
            weatherService.getWeather("paris");

            // Then
            verify(weatherClient, times(1)).getCurrentWeather("paris");
        }

        @Test
        @DisplayName("Should handle empty string input (pass empty string to client)")
        void getWeather_CallsClientWithEmpty() {
            // Given
            String emptyInput = "    ";
            when(weatherClient.getCurrentWeather("")).thenReturn(mockResponse);

            // When
            weatherService.getWeather(emptyInput);

            // Then
            verify(weatherClient).getCurrentWeather("");
        }
    }
}
