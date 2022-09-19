package com.joanna.staybooking.config;

import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleGeoCodingConfig {

    @Value("${geocoding.apikey}")
    private String apiKey;

    //The GeoApiContext is designed to be a Singleton in your application.
    //Instantiate one on application startup, and continue to use it for the life of your application
    @Bean
    public GeoApiContext geoApiContext() {
        return new GeoApiContext.Builder().apiKey(apiKey).build();

    }
}

