package com.climastyle.service;

import com.climastyle.client.N8nClient;
import com.climastyle.dto.WeatherRequestDTO;
import com.climastyle.dto.WeatherResponseDTO;
import java.text.Normalizer;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class WeatherService {

  // Se comunica con n8n para obtener clima + recomendacion.
  private final N8nClient n8nClient;
  public WeatherService(N8nClient n8nClient) {
    this.n8nClient = n8nClient;
  }

  public WeatherResponseDTO getWeatherAndRecommendation(String city) {
    // Paso 1: normalizar la ciudad (acentos/espacios) antes de enviar a n8n.
    String normalizedCity = normalizeCity(city);
    if (normalizedCity == null || normalizedCity.trim().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ciudad requerida");
    }
    // Paso 2: solicitar a n8n el clima y la recomendacion.
    WeatherResponseDTO response;
    try {
      response = n8nClient.requestWeather(new WeatherRequestDTO(normalizedCity));
    } catch (IllegalStateException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "No se pudo consultar el servicio de clima");
    }

    if (response == null || response.getCity() == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ciudad no encontrada");
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

