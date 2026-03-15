package com.climastyle.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "weather_queries")
public class WeatherQuery {

  // Entidad persistida con el historial de consultas.
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String city;

  @Column(precision = 5, scale = 2)
  private BigDecimal temperature;

  @Column(name = "`condition`", length = 100)
  private String weatherCondition;

  @Column(columnDefinition = "TEXT")
  private String recommendation;

  @Column
  private Integer humidity;

  @Column(precision = 5, scale = 2)
  private BigDecimal wind;

  @Column(name = "feels_like", precision = 5, scale = 2)
  private BigDecimal feelsLike;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @PrePersist
  public void prePersist() {
    // Marca la fecha de creacion en la primera insercion.
    if (createdAt == null) {
      createdAt = LocalDateTime.now();
    }
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public BigDecimal getTemperature() {
    return temperature;
  }

  public void setTemperature(BigDecimal temperature) {
    this.temperature = temperature;
  }

  public String getWeatherCondition() {
    return weatherCondition;
  }

  public void setWeatherCondition(String weatherCondition) {
    this.weatherCondition = weatherCondition;
  }

  public String getRecommendation() {
    return recommendation;
  }

  public void setRecommendation(String recommendation) {
    this.recommendation = recommendation;
  }

  public Integer getHumidity() {
    return humidity;
  }

  public void setHumidity(Integer humidity) {
    this.humidity = humidity;
  }

  public BigDecimal getWind() {
    return wind;
  }

  public void setWind(BigDecimal wind) {
    this.wind = wind;
  }

  public BigDecimal getFeelsLike() {
    return feelsLike;
  }

  public void setFeelsLike(BigDecimal feelsLike) {
    this.feelsLike = feelsLike;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
