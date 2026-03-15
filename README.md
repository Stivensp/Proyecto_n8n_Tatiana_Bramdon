# Proyecto_n8n_Tatiana_Bramdon
Asistente inteligente que consulta el clima de una ciudad y genera recomendaciones de vestimenta segun las condiciones meteorologicas actuales. Utiliza n8n para la automatizacion de flujos, Open-Meteo para obtener datos en tiempo real, MySQL para almacenar consultas y MCP como servidor de herramientas (opcional). El flujo devuelve ciudad, temperatura, humedad, viento, sensacion termica, condicion y recomendacion.

## Integrantes
- Tatiana Villamizar
- Bramdon Blanco

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
- `MYSQL_PORT` (default: 3309)
- `MYSQL_DB` (default: weatherdb)
- `MYSQL_USER` (default: root)
- `MYSQL_PASSWORD` (default: admin)
- `N8N_WEBHOOK_URL` (default: http://localhost:5678/webhook/weather)
- `N8N_WEBHOOK_TEST_URL` (default: http://localhost:5678/webhook-test/weather)

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

## API Backend
Endpoint: `POST http://localhost:3001/api/weather-outfit`
Body:
```json
{ "city": "Bogota" }
```
Respuesta (ejemplo):
```json
{
  "city": "Giron",
  "temperature": 26.3,
  "feels_like": 32.2,
  "humidity": 83,
  "wind": 3.7,
  "condition": "Parcialmente nublado",
  "recommendation": "..."
}
```

## n8n
1. Inicia n8n en `http://localhost:5678`.
2. Importa el workflow desde `docs/n8n-workflow.json`.
3. Configura las credenciales de MySQL en el nodo MySQL.
4. Activa el workflow.

## Instrucciones para configurar el workflow completo de n8n con MySQL
1. Preparar el entorno. Instala Docker y Docker Compose.
2. Verifica Docker con `docker --version`.
3. Verifica Docker Compose con `docker-compose --version`.
4. Crea la red con `docker network create n8n-network`.
5. Levanta MySQL con:
`docker run -d --name mysql_container --network n8n-network -e MYSQL_ROOT_PASSWORD=admin -e MYSQL_DATABASE=weatherdb -p 3309:3306 mysql:8`
6. Verifica que MySQL esta corriendo con `docker ps`.
7. Conecta al contenedor con `docker exec -it mysql_container mysql -u root -p` y usa password `admin`.
8. Crea la tabla:
```sql
CREATE TABLE weather_queries (
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
```

9. Levanta n8n conectado a MySQL:
```
docker run -d --name n8n_prod --network n8n-network -p 5678:5678 -e DB_TYPE=mysql -e DB_MYSQLDB=weatherdb -e DB_MYSQLUSER=root -e DB_MYSQLPASSWORD=admin -e DB_MYSQLHOST=mysql_container -e N8N_BASIC_AUTH_ACTIVE=true -e N8N_BASIC_AUTH_USER=admin -e N8N_BASIC_AUTH_PASSWORD=admin123 n8nio/n8n
```

Panel: `http://localhost:5678`
Usuario/contraseña: `admin/admin123`

10. Importa el workflow en n8n: Workflows -> Import from File -> `docs/n8n-workflow.json`.
11. Guarda y activa el workflow.
12. Configura el webhook: Path `weather`, URL `http://localhost:5678/webhook/weather`, Method `POST`.
13. Prueba el workflow:
```
curl -X POST http://localhost:5678/webhook/weather -H "Content-Type: application/json" -d "{\"city\":\"Bogota\"}"
```

14. Ver datos con:
`docker exec -it mysql_container mysql -u root -p`
`USE weatherdb;`
`SELECT * FROM weather_queries;`
15. Detener: `docker stop n8n_prod mysql_container`.
16. Iniciar: `docker start mysql_container n8n_prod`.

## Flujo
Frontend -> Backend -> n8n -> Open-Meteo + MySQL -> Backend -> Frontend
