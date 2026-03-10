package com.climastyle.client;

import com.climastyle.dto.WeatherRequestDTO;
import com.climastyle.dto.WeatherResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class N8nClient {

  private final RestTemplate restTemplate;
  private final String webhookUrl;

  public N8nClient(RestTemplate restTemplate, @Value("${app.n8n.webhook-url}") String webhookUrl) {
    this.restTemplate = restTemplate;
    this.webhookUrl = webhookUrl;
  }

  public WeatherResponseDTO requestWeather(WeatherRequestDTO request) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<WeatherRequestDTO> entity = new HttpEntity<>(request, headers);

    ResponseEntity<WeatherResponseDTO> response =
        restTemplate.postForEntity(webhookUrl, entity, WeatherResponseDTO.class);

    if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
      throw new IllegalStateException("Failed to get response from n8n webhook");
    }

    return response.getBody();
  }
}
