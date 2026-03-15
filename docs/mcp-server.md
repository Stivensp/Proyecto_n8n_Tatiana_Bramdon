# MCP Server

El servidor MCP expone herramientas HTTP simples para probar y reutilizar logica de clima/outfit.

## Endpoints
- `GET /health`  
  Respuesta: `{ "status": "ok" }`

- `POST /tool/get_weather`  
  Body:
  ```json
  { "city": "Bogota" }
  ```
  Respuesta:
  ```json
  { "city": "Bogota", "temperature": 22.1, "condition": "parcialmente nublado" }
  ```

- `POST /tool/recommend_outfit`  
  Body:
  ```json
  { "temperature": 22, "condition": "lluvia" }
  ```
  Respuesta:
  ```json
  { "recommendation": "ropa comoda y paraguas" }
  ```
