# System Design

## Backend
- **Controller**: recibe la ciudad y delega al servicio.
- **Service**: normaliza la ciudad, llama a n8n y persiste en MySQL.
- **Client**: `WeatherApiClient` maneja la comunicacion con n8n y reintentos.
- **Repository/Model**: persistencia con JPA en `weather_queries`.
- **Config**: `RestTemplateConfig` y `CorsConfig`.

## Integraciones
- **n8n**: genera el JSON final con outfit y recomendacion.
- **Open-Meteo**: fuente de datos meteorologicos.
- **MySQL**: almacenamiento del historial.
- **MCP Server**: herramientas auxiliares para pruebas de clima/outfit.

## Consideraciones
- Timeouts en llamadas a n8n para evitar bloqueos.
- Respuestas de error claras al frontend.
