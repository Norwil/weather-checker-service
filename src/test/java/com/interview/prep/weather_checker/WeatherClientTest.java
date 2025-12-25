package com.interview.prep.weather_checker;

import com.interview.prep.weather_checker.client.WeatherClient;
import com.interview.prep.weather_checker.dto.WeatherResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(WeatherClient.class)
public class WeatherClientTest {

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @Autowired
    private WeatherClient weatherClient;

    @TestConfiguration
    static class TestConfig {

        @Bean
        public RestClient weatherRestClient(RestClient.Builder builder) {
            return builder.
                    baseUrl("https://api.open-meteo.com")
                    .build();
        }
    }

    @Test
    void shouldReturnWeather_WhenCityIsFound() {
        // Arrange
        mockRestServiceServer
                .expect(requestTo("https://geocoding-api.open-meteo.com/v1/search?name=Berlin&count=1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                    {
                      "results": [
                        {
                          "name": "Berlin",
                          "latitude": 52.52,
                          "longitude": 13.41
                        }
                      ]
                    }
                    """, MediaType.APPLICATION_JSON));

        mockRestServiceServer
                .expect(requestTo("https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&current_weather=true"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                        {
                          "current_weather": {
                            "temperature": 15.0,
                            "windspeed": 10.5,
                            "weathercode": 1
                          }
                        }
                        """, MediaType.APPLICATION_JSON));

        // Act
        WeatherResponse actualResponse = weatherClient.getCurrentWeather("Berlin");

        // Assert
        assertEquals(15.0, actualResponse.currentWeather().temperature());
        assertEquals(10.5, actualResponse.currentWeather().windspeed());
        assertEquals(1, actualResponse.currentWeather().weathercode());
    }

}
