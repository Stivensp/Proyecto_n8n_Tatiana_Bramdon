package com.climastyle.client;

import com.climastyle.dto.WeatherRequestDTO;
import com.climastyle.dto.WeatherResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherApiClient {

  // Cliente HTTP usado para invocar el webhook de n8n.
  private final RestTemplate restTemplate;
  private final String webhookUrl;
  private final String webhookTestUrl;
  private final ObjectMapper objectMapper;

  public WeatherApiClient(
      RestTemplate restTemplate,
      ObjectMapper objectMapper,
      @Value("${app.n8n.webhook-url}") String webhookUrl,
      @Value("${app.n8n.webhook-url-test}") String webhookTestUrl) {
    this.restTemplate = restTemplate;
    this.objectMapper = objectMapper;
    this.webhookUrl = webhookUrl;
    this.webhookTestUrl = webhookTestUrl;
  }

  // Llama a n8n y devuelve el DTO ya mapeado.
  public WeatherResponseDTO requestWeather(WeatherRequestDTO request) {
    // Primero intenta el webhook principal y, si no existe, usa el de prueba.
    HttpEntity<WeatherRequestDTO> entity = buildEntity(request);

    try {
      return postWithRetry(webhookUrl, entity, 2);
    } catch (HttpClientErrorException.NotFound notFound) {
      return postWithRetry(webhookTestUrl, entity, 2);
    }
  }

  private HttpEntity<WeatherRequestDTO> buildEntity(WeatherRequestDTO request) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new HttpEntity<>(request, headers);
  }

  private WeatherResponseDTO postForWeather(String url, HttpEntity<WeatherRequestDTO> entity) {
    // Llama al webhook de n8n y mapea la respuesta JSON al DTO.
    ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

    if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
      throw new IllegalStateException("Failed to get response from n8n webhook");
    }

    String body = response.getBody();
    WeatherResponseDTO mapped = mapResponse(body);
    if (mapped == null) {
      throw new IllegalStateException("Failed to get response from n8n webhook");
    }
    return mapped;
  }

  // Reintento simple para errores temporales de red.
  private WeatherResponseDTO postWithRetry(String url, HttpEntity<WeatherRequestDTO> entity, int maxAttempts) {
    RestClientException lastError = null;
    for (int attempt = 1; attempt <= maxAttempts; attempt++) {
      try {
        return postForWeather(url, entity);
      } catch (RestClientException ex) {
        lastError = ex;
        try {
          Thread.sleep(300L * attempt);
        } catch (InterruptedException interrupted) {
          Thread.currentThread().interrupt();
          break;
        }
      }
    }
    if (lastError != null) {
      throw lastError;
    }
    throw new IllegalStateException("Failed to get response from n8n webhook");
  }

  private WeatherResponseDTO mapResponse(String body) {
    // Soporta respuesta como array o como objeto unico.
    try {
      JsonNode node = objectMapper.readTree(body);
      if (node.isArray() && node.size() > 0) {
        return objectMapper.treeToValue(node.get(0), WeatherResponseDTO.class);
      }
      if (node.isObject()) {
        return objectMapper.treeToValue(node, WeatherResponseDTO.class);
      }
      return null;
    } catch (Exception ex) {
      return null;
    }
  }
}

