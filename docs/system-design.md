# System Design

## Backend (Spring Boot)
- Endpoint: `POST /api/weather-outfit`.
- Recibe `{ "city": "Bogota" }`.
- Llama a n8n usando `N8nClient`.
- Persiste consulta en MySQL con `WeatherQueryRepository`.
- Retorna `WeatherResponseDTO` al frontend.

## MCP Server (Node.js + Express)
- `POST /tool/get_weather`: consulta Open-Meteo (geocoding + current weather).
- `POST /tool/recommend_outfit`: aplica reglas simples de vestimenta.
- Se utiliza desde n8n como paso intermedio o servicio auxiliar.

## Base de datos
- MySQL con tabla `weather_queries`.
- Schema en `database/schema.sql`.

## Workflow n8n
1. Webhook (POST `/webhook/weather`).
2. HTTP Request: geocoding Open-Meteo.
3. HTTP Request: weather Open-Meteo usando lat/lon.
4. Code Node: genera recomendacion con reglas de temperatura y lluvia.
5. MySQL Node: inserta en `weather_queries`.
6. Respond to Webhook: retorna `{ city, temperature, condition, recommendation }`.

## Configuracion
Backend (`backend/src/main/resources/application.yml`):
- `MYSQL_HOST`, `MYSQL_PORT`, `MYSQL_DB`, `MYSQL_USER`, `MYSQL_PASSWORD`.
- `N8N_WEBHOOK_URL`.

MCP Server:
- `PORT` (default 4000).

## Errores
- Backend devuelve error 5xx si n8n falla o devuelve respuesta invalida.
- MCP Server devuelve 400 en payloads incompletos.
