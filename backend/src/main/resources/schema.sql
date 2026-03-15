CREATE TABLE IF NOT EXISTS weather_queries (
  id INT AUTO_INCREMENT PRIMARY KEY,
  city VARCHAR(100) NOT NULL,
  temperature DECIMAL(5,2) NOT NULL,
  `condition` VARCHAR(100) NOT NULL,
  recommendation TEXT,
  humidity INT,
  wind DECIMAL(5,2),
  feels_like DECIMAL(5,2),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
