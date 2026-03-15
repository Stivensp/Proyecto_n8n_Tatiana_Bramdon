package com.climastyle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;

@SpringBootApplication
public class ClimastyleApplication {

  // Punto de entrada del backend. Levanta Spring y habilita los beans.
  public static void main(String[] args) {
    SpringApplication.run(ClimastyleApplication.class, args);
  }

  // RestTemplate reutilizado por el cliente que llama a n8n.
  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder
        .setConnectTimeout(Duration.ofSeconds(5))
        .setReadTimeout(Duration.ofSeconds(10))
        .build();
  }
}
