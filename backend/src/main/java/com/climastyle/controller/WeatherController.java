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

  // Flujo principal (orden): Controller -> Service -> n8n -> respuesta al Frontend.
  private final WeatherService weatherService;

  public WeatherController(WeatherService weatherService) {
    this.weatherService = weatherService;
  }

  @PostMapping("/weather-outfit")
  public ResponseEntity<WeatherResponseDTO> getWeatherOutfit(
      @Valid @RequestBody WeatherRequestDTO request) {
    // 1) Recibe la ciudad desde el frontend.
    // 2) Delegar al servicio para consultar n8n.
    WeatherResponseDTO response = weatherService.getWeatherAndRecommendation(request.getCity());
    // 3) Responde al frontend con los datos del clima + recomendacion.
    return ResponseEntity.ok(response);
  }
}

