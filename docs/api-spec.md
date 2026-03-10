# API Spec

## Backend
### POST /api/weather-outfit
Request:
```json
{
  "city": "Bogota"
}
```

Response 200:
```json
{
  "city": "Bogota",
  "temperature": 18.3,
  "condition": "parcialmente nublado",
  "recommendation": "ropa comoda"
}
```

Errores:
- 400 si `city` es vacio.
- 5xx si falla la integracion con n8n.

## MCP Server
### POST /tool/get_weather
Request:
```json
{
  "city": "Bogota"
}
```

Response:
```json
{
  "city": "Bogota",
  "temperature": 18.3,
  "condition": "parcialmente nublado"
}
```

### POST /tool/recommend_outfit
Request:
```json
{
  "temperature": 18.3,
  "condition": "parcialmente nublado"
}
```

Response:
```json
{
  "recommendation": "ropa comoda"
}
```
