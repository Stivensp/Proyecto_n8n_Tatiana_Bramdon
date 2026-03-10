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
import org.springframework.web.client.RestTemplate;

@Component
public class N8nClient {

  private final RestTemplate restTemplate;
  private final String webhookUrl;
  private final String webhookTestUrl;
  private final ObjectMapper objectMapper;

  public N8nClient(
      RestTemplate restTemplate,
      ObjectMapper objectMapper,
      @Value("${app.n8n.webhook-url}") String webhookUrl,
      @Value("${app.n8n.webhook-url-test}") String webhookTestUrl) {
    this.restTemplate = restTemplate;
    this.objectMapper = objectMapper;
    this.webhookUrl = webhookUrl;
    this.webhookTestUrl = webhookTestUrl;
  }

  public WeatherResponseDTO requestWeather(WeatherRequestDTO request) {
    HttpEntity<WeatherRequestDTO> entity = buildEntity(request);

    try {
      return postForWeather(webhookUrl, entity);
    } catch (HttpClientErrorException.NotFound notFound) {
      return postForWeather(webhookTestUrl, entity);
    }
  }

  private HttpEntity<WeatherRequestDTO> buildEntity(WeatherRequestDTO request) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new HttpEntity<>(request, headers);
  }

  private WeatherResponseDTO postForWeather(String url, HttpEntity<WeatherRequestDTO> entity) {
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

  private WeatherResponseDTO mapResponse(String body) {
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
