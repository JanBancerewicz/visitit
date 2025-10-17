package com.example.visitit.category.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    WebClient elementsClient(@Value("${elements.base-url}") String base) {
        return WebClient.builder().baseUrl(base).build();
    }
}
