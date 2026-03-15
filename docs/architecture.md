# Arquitectura

## Componentes
- **Frontend**: interfaz web para consultar el clima y mostrar recomendaciones/outfit.
- **Backend (Spring Boot)**: expone la API `/api/weather-outfit`, valida la ciudad, llama a n8n y guarda el historial en MySQL.
- **n8n**: orquesta la consulta a Open-Meteo y genera el JSON de salida con outfit.
- **MySQL**: almacena el historial de consultas.

## Flujo principal
1. El usuario consulta una ciudad desde el frontend.
2. El backend normaliza la ciudad y llama al webhook de n8n.
3. n8n consulta Open-Meteo, construye el outfit y devuelve el JSON.
4. El backend guarda la consulta en MySQL y responde al frontend.
