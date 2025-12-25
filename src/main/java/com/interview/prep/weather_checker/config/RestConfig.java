package com.interview.prep.weather_checker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestConfig {

    @Bean
    public RestClient weatherRestClient(RestClient.Builder builder) {
        // Define a connection and read timeout
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(3000);

        return builder
                .baseUrl("https://api.open-meteo.com")
                .requestFactory(factory)
                .build();
    }


}
