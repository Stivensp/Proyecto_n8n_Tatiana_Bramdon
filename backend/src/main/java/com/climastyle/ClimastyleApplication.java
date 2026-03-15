package com.climastyle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClimastyleApplication {

  // Punto de entrada del backend. Levanta Spring y habilita los beans.
  public static void main(String[] args) {
    SpringApplication.run(ClimastyleApplication.class, args);
  }

}
