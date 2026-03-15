package com.climastyle.service;

import com.climastyle.client.WeatherApiClient;
import com.climastyle.dto.WeatherRequestDTO;
import com.climastyle.dto.WeatherResponseDTO;
import java.text.Normalizer;
import org.springframework.stereotype.Service;
import com.climastyle.model.WeatherQuery;
import com.climastyle.repository.WeatherQueryRepository;
import com.climastyle.util.ClothingRecommendationUtil;
import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class WeatherService {

  // Se comunica con n8n para obtener clima + recomendacion.
  private final WeatherApiClient weatherApiClient;
  private final WeatherQueryRepository repository;
  private final ClothingRecommendationUtil recommendationUtil;

  public WeatherService(
      WeatherApiClient weatherApiClient,
      WeatherQueryRepository repository,
      ClothingRecommendationUtil recommendationUtil) {
    this.weatherApiClient = weatherApiClient;
    this.repository = repository;
    this.recommendationUtil = recommendationUtil;
  }

  // Orquesta el flujo: validar -> llamar n8n -> fallback -> persistir -> responder.
  public WeatherResponseDTO getWeatherAndRecommendation(String city) {
    // Paso 1: normalizar la ciudad (acentos/espacios) antes de enviar a n8n.
    String normalizedCity = normalizeCity(city);
    if (normalizedCity == null || normalizedCity.trim().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ciudad requerida");
    }
    // Paso 2: solicitar a n8n el clima y la recomendacion.
    WeatherResponseDTO response;
    try {
      response = weatherApiClient.requestWeather(new WeatherRequestDTO(normalizedCity));
    } catch (IllegalStateException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "No se pudo consultar el servicio de clima");
    }

    if (response == null || response.getCity() == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ciudad no encontrada");
    }

    // Fallback si n8n no retorna recomendacion.
    if (response.getRecommendation() == null || response.getRecommendation().trim().isEmpty()) {
      response.setRecommendation(recommendationUtil.buildRecommendation(response));
    }

    // Persistencia en MySQL.
    if (response.getTemperature() != null) {
      WeatherQuery query = new WeatherQuery();
      query.setCity(normalizedCity);
      query.setTemperature(BigDecimal.valueOf(response.getTemperature()));
      query.setWeatherCondition(response.getCondition());
      query.setRecommendation(response.getRecommendation());
      query.setHumidity(response.getHumidity());
      if (response.getWind() != null) {
        query.setWind(BigDecimal.valueOf(response.getWind()));
      }
      if (response.getFeelsLike() != null) {
        query.setFeelsLike(BigDecimal.valueOf(response.getFeelsLike()));
      }
      repository.save(query);
    }

    // Paso 3: devolver la respuesta al controller.
    return response;
  }

  private static String normalizeCity(String city) {
    // Normaliza entrada para evitar problemas de busqueda (acentos/espacios).
    if (city == null) {
      return null;
    }
    String trimmed = city.trim();
    String normalized = Normalizer.normalize(trimmed, Normalizer.Form.NFD)
        .replaceAll("\\p{M}", "");
    normalized = normalized.replaceAll("[^\\p{ASCII}]", "");
    normalized = normalized.replaceAll("\\s+", " ").trim();
    return normalized;
  }
}
