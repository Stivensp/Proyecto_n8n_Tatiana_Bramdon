# Proyecto_n8n_Tatiana_Bramdon
Asistente inteligente que consulta el clima de una ciudad y genera recomendaciones de vestimenta segun las condiciones meteorologicas actuales. Utiliza n8n para la automatizacion de flujos, Open-Meteo para obtener datos en tiempo real, MySQL para almacenar consultas y MCP para estructurar la interaccion entre servicios.

## Requisitos
- Java 17
- Maven
- Node.js 18+
- MySQL
- n8n

## Base de datos
Ejecuta el script:
```
mysql -u root -p < database/schema.sql
```

## Variables de entorno (opcional)
Backend (`backend/src/main/resources/application.yml`):
- `MYSQL_HOST` (default: localhost)
- `MYSQL_PORT` (default: 3306)
- `MYSQL_DB` (default: climastyle_db)
- `MYSQL_USER` (default: root)
- `MYSQL_PASSWORD` (default: root)
- `N8N_WEBHOOK_URL` (default: http://localhost:5678/webhook/weather)

MCP Server:
- `PORT` (default: 4000)

## MCP Server
```
cd mcp-server
npm install
npm start
```

## Backend
```
cd backend
mvn spring-boot:run
```

## Frontend
Abrir `frontend/index.html` en el navegador o servirlo con cualquier server estatico.

## n8n
1. Inicia n8n en `http://localhost:5678`.
2. Importa el workflow desde `docs/n8n-workflow.json`.
3. Configura las credenciales de MySQL en el nodo MySQL.
4. Activa el workflow.

## Flujo
Frontend -> Backend -> n8n -> Open-Meteo + MySQL -> Backend -> Frontend
