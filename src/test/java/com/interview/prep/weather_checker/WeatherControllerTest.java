package com.interview.prep.weather_checker;

import com.interview.prep.weather_checker.controller.WeatherController;
import com.interview.prep.weather_checker.dto.CurrentWeather;
import com.interview.prep.weather_checker.dto.WeatherResponse;
import com.interview.prep.weather_checker.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
public class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WeatherService weatherService;

    @Test
    void shouldReturnWeatherForBerlin() throws Exception {
        // Arrange
        CurrentWeather mockData = new CurrentWeather(10.0, 5.5, 1);
        WeatherResponse mockResponse = new WeatherResponse(mockData);

        when(weatherService.getWeather("Berlin")).thenReturn(mockResponse);

        mockMvc.perform(get("/weather/Berlin"))
                .andExpect(status().isOk());
    }

}