package com.climastyle.util;

import com.climastyle.dto.WeatherResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ClothingRecommendationUtil {

  // Fallback simple cuando n8n no devuelve recomendacion.
  public String buildRecommendation(WeatherResponseDTO response) {
    if (response == null || response.getFeelsLike() == null) {
      return "Sin recomendacion disponible.";
    }

    double feelsLike = response.getFeelsLike();
    if (feelsLike >= 30) {
      return "Hace bastante calor. Usa ropa ligera y mantente hidratado.";
    }
    if (feelsLike >= 24) {
      return "Clima calido. Ropa fresca es recomendable.";
    }
    if (feelsLike >= 18) {
      return "Temperatura agradable. Ropa ligera es suficiente.";
    }
    if (feelsLike >= 10) {
      return "Hace fresco. Considera una chaqueta ligera.";
    }
    return "Hace frio. Usa abrigo y ropa termica.";
  }
}
