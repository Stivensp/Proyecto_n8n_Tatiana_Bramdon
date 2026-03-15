# API Spec

## POST /api/weather-outfit
Consulta clima y devuelve recomendacion + outfit.

### Request
```json
{
  "city": "Bogota"
}
```

### Response 200 (ejemplo)
```json
{
  "city": "Bogota",
  "temperature": 22.1,
  "feels_like": 24.0,
  "humidity": 78,
  "wind": 3.7,
  "condition": "Parcialmente nublado",
  "recommendation": "Clima calido. Ropa fresca es recomendable.",
  "outfit": {
    "top": "Camiseta ligera",
    "bottom": "Shorts",
    "shoes": "Sandalias",
    "accessories": ["Sombrero", "Gafas de sol"]
  }
}
```

### Errores
- `400` Ciudad requerida.
- `404` Ciudad no encontrada.
- `502` No se pudo consultar el servicio de clima.
