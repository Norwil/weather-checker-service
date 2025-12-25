package com.interview.prep.weather_checker.client;

import com.interview.prep.weather_checker.dto.GeocodingResponse;
import com.interview.prep.weather_checker.dto.Location;
import com.interview.prep.weather_checker.dto.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Component
@Slf4j
public class WeatherClient {


    private final RestClient restClient;
    private final String geocodingUrl;      //

    public WeatherClient(RestClient restClient,
                         @Value("${weather.api.geocoding-url}") String geocodingUrl) {
        this.restClient = restClient;
        this.geocodingUrl = geocodingUrl;
    }

    public WeatherResponse getCurrentWeather(String city) {

        Location location = fetchCoordinates(city);

        String uriTemplate = "/v1/forecast?latitude={lat}&longitude={lon}&current_weather=true";

        log.debug(
                "Calling Open-Meteo API Weather API with lat={}, lon={}",
                location.latitude(),
                location.longitude()
        );

        return restClient.get()
                .uri(uriTemplate, location.latitude(), location.longitude())
                .retrieve()
                .body(WeatherResponse.class);
    }

    private Location fetchCoordinates(String city) {

        String uriTemplate = geocodingUrl + "/v1/search?name={city}&count=1";

        log.debug("Calling Open-Meteo Geocoding API for city={}", city);

        GeocodingResponse response = restClient.get()
                .uri(uriTemplate, city)
                .retrieve()
                .body(GeocodingResponse.class);

        if (response == null || response.results() == null || response.results().isEmpty()) {
            throw new IllegalArgumentException("City not found. " + city);
        }

        return response.results().get(0);
    }

}
