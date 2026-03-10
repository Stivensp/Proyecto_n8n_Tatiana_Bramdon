package com.climastyle.repository;

import com.climastyle.model.WeatherQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherQueryRepository extends JpaRepository<WeatherQuery, Long> {}
