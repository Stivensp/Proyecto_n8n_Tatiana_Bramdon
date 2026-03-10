# Arquitectura

## Componentes
- Frontend estatico en `frontend/`.
- Backend Spring Boot en `backend/` (puerto 3001).
- n8n en `http://localhost:5678` con webhook `POST /webhook/weather`.
- MCP Server Node.js en `mcp-server/` (puerto 4000) con herramientas para clima y recomendacion.
- MySQL con base `climastyle_db` y tabla `weather_queries`.

## Flujo
1. Usuario ingresa ciudad en el frontend.
2. Frontend llama `POST http://localhost:3001/api/weather-outfit`.
3. Backend llama webhook n8n `POST http://localhost:5678/webhook/weather` con `{ "city": "Bogota" }`.
4. n8n consulta Open-Meteo (geocoding + weather), calcula recomendacion y guarda en MySQL.
5. n8n responde `{ city, temperature, condition, recommendation }`.
6. Backend persiste una copia en MySQL (JPA) y devuelve respuesta al frontend.

## Puertos
- Frontend: estatico (archivo local o servidor estatico)
- Backend: `3001`
- MCP Server: `4000`
- n8n: `5678`
- MySQL: `3306`
