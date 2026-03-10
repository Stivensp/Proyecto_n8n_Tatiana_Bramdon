package com.climastyle.controller;

import com.climastyle.dto.WeatherRequestDTO;
import com.climastyle.dto.WeatherResponseDTO;
import com.climastyle.service.WeatherService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class WeatherController {

  private final WeatherService weatherService;

  public WeatherController(WeatherService weatherService) {
    this.weatherService = weatherService;
  }

  @PostMapping("/weather-outfit")
  public ResponseEntity<WeatherResponseDTO> getWeatherOutfit(
      @Valid @RequestBody WeatherRequestDTO request) {
    WeatherResponseDTO response = weatherService.getWeatherAndRecommendation(request.getCity());
    return ResponseEntity.ok(response);
  }
}
