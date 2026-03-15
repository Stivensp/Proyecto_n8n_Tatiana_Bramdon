package com.climastyle.repository;

import com.climastyle.model.WeatherQuery;
import org.springframework.data.jpa.repository.JpaRepository;

// Acceso CRUD a la tabla weather_queries.
public interface WeatherQueryRepository extends JpaRepository<WeatherQuery, Long> {}
