package com.climastyle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherResponseDTO {

  private String city;
  private Double temperature;
  private Double feelsLike;
  private Integer humidity;
  private Double wind;
  private String condition;
  private String recommendation;

  public WeatherResponseDTO() {}

  public WeatherResponseDTO(String city, Double temperature, Double feelsLike, Integer humidity, Double wind,
      String condition, String recommendation) {
    this.city = city;
    this.temperature = temperature;
    this.feelsLike = feelsLike;
    this.humidity = humidity;
    this.wind = wind;
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

  @JsonProperty("feels_like")
  public Double getFeelsLike() {
    return feelsLike;
  }

  @JsonProperty("feels_like")
  public void setFeelsLike(Double feelsLike) {
    this.feelsLike = feelsLike;
  }

  public Integer getHumidity() {
    return humidity;
  }

  public void setHumidity(Integer humidity) {
    this.humidity = humidity;
  }

  public Double getWind() {
    return wind;
  }

  public void setWind(Double wind) {
    this.wind = wind;
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
