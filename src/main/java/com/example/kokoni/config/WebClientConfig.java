package com.example.kokoni.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
            .baseUrl("https://api.mangadex.org")
            .defaultHeader("User-Agent", "KokoniApp/1.0 (contact@kokoni.com)")
            .build();
    }
}