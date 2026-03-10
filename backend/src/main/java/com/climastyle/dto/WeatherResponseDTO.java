package com.climastyle.dto;

public class WeatherResponseDTO {

  private String city;
  private Double temperature;
  private String condition;
  private String recommendation;

  public WeatherResponseDTO() {}

  public WeatherResponseDTO(String city, Double temperature, String condition, String recommendation) {
    this.city = city;
    this.temperature = temperature;
    this.condition = condition;
    this.recommendation = recommendation;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public Double getTemperature() {
    return temperature;
  }

  public void setTemperature(Double temperature) {
    this.temperature = temperature;
  }

  public String getCondition() {
    return condition;
  }

  public void setCondition(String condition) {
    this.condition = condition;
  }

  public String getRecommendation() {
    return recommendation;
  }

  public void setRecommendation(String recommendation) {
    this.recommendation = recommendation;
  }
}
