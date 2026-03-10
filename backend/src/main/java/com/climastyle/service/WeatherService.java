package com.climastyle.service;

import com.climastyle.client.N8nClient;
import com.climastyle.dto.WeatherRequestDTO;
import com.climastyle.dto.WeatherResponseDTO;
import com.climastyle.model.WeatherQuery;
import com.climastyle.repository.WeatherQueryRepository;
import java.math.BigDecimal;
import java.text.Normalizer;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

  private final N8nClient n8nClient;
  private final WeatherQueryRepository repository;

  public WeatherService(N8nClient n8nClient, WeatherQueryRepository repository) {
    this.n8nClient = n8nClient;
    this.repository = repository;
  }

  public WeatherResponseDTO getWeatherAndRecommendation(String city) {
    String normalizedCity = normalizeCity(city);
    WeatherResponseDTO response = n8nClient.requestWeather(new WeatherRequestDTO(normalizedCity));

    if (response.getTemperature() != null) {
      WeatherQuery query = new WeatherQuery();
      query.setCity(normalizedCity);
      query.setTemperature(BigDecimal.valueOf(response.getTemperature()));
      query.setWeatherCondition(response.getCondition());
      query.setRecommendation(response.getRecommendation());
      repository.save(query);
    }

    return response;
  }

  private static String normalizeCity(String city) {
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
