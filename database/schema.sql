CREATE DATABASE weatherdb;

USE weatherdb;

CREATE TABLE weather_queries (
  id INT AUTO_INCREMENT PRIMARY KEY,
  city VARCHAR(100),
  temperature DECIMAL(5,2),
  weather_condition VARCHAR(100),
  recommendation TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
