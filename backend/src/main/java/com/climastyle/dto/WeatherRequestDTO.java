package com.climastyle.dto;

import jakarta.validation.constraints.NotBlank;

public class WeatherRequestDTO {

  @NotBlank(message = "city is required")
  private String city;

  public WeatherRequestDTO() {}

  public WeatherRequestDTO(String city) {
    this.city = city;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }
}
