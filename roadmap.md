Paso a paso para desarrollar el proyecto
1. Preparar el entorno de desarrollo

Primero instala las herramientas necesarias.

Software requerido

Instalar Node.js

Instalar MySQL

Instalar n8n

Instalar Java + Spring Boot

Crear cuenta en una API meteorológica como

OpenWeatherMap

WeatherAPI

Empieza ejecutando n8n:

npx n8n

Luego abre:

http://localhost:5678
2. Crear la base de datos MySQL

Crear la base de datos:

CREATE DATABASE weather_assistant;

Seleccionarla:

USE weather_assistant;

Crear la tabla de consultas:

CREATE TABLE weather_queries (
    id INT AUTO_INCREMENT PRIMARY KEY,
    city VARCHAR(100) NOT NULL,
    temperature DECIMAL(5,2) NOT NULL,
    weather_condition VARCHAR(100) NOT NULL,
    recommended_clothes TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

Probar conexión desde terminal o cliente MySQL.

3. Crear el Backend con Spring Boot

Crear un proyecto en Spring Initializr.

Dependencias:

Spring Web

Spring Data JPA

MySQL Driver

Lombok (opcional)

Configurar conexión a MySQL

En application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/weather_assistant
spring.datasource.username=root
spring.datasource.password=tu_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
Crear modelo de datos

WeatherQuery.java

@Entity
public class WeatherQuery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private Double temperature;
    private String weatherCondition;

    @Column(length = 500)
    private String recommendedClothes;

    private LocalDateTime createdAt = LocalDateTime.now();
}
Crear Repository

WeatherQueryRepository.java

@Repository
public interface WeatherQueryRepository
        extends JpaRepository<WeatherQuery, Long> {
}
Crear Controller

WeatherController.java

@RestController
@RequestMapping("/api")
public class WeatherController {

    @PostMapping("/weather-outfit")
    public Map<String,Object> consultWeather(@RequestBody Map<String,String> body){

        String city = body.get("city");

        Map<String,Object> response = new HashMap<>();
        response.put("city", city);
        response.put("temperature", 20);
        response.put("condition", "Soleado");
        response.put("recommendation", "Usa ropa ligera");

        return response;
    }
}

Probar con Postman:

POST http://localhost:8080/api/weather-outfit

Body:

{
 "city":"Bogotá"
}
4. Crear el frontend

Crear carpeta:

frontend/

Archivos:

index.html
styles.css
script.js

Abrir index.html en navegador.

Probar que el botón haga la solicitud al backend.

5. Integrar la API meteorológica

Ejemplo con OpenWeatherMap.

URL:

https://api.openweathermap.org/data/2.5/weather?q=Bogota&appid=API_KEY&units=metric

Ejemplo de respuesta:

{
 "main":{
   "temp":18
 },
 "weather":[
   {"description":"nublado"}
 ]
}
6. Crear el workflow en n8n

Entrar a n8n y crear un workflow.

Nodo 1 — Webhook

Tipo:

POST

Path:

weather-outfit
Nodo 2 — HTTP Request

Consulta la API meteorológica.

URL:

https://api.openweathermap.org/data/2.5/weather

Query:

q={{$json.city}}
appid=API_KEY
units=metric
Nodo 3 — Function

Procesa la respuesta.

const temp = $json.main.temp;
const condition = $json.weather[0].description;

let recommendation = "";

if (temp < 10) {
  recommendation = "Usa abrigo grueso";
}
else if (temp < 18) {
  recommendation = "Usa chaqueta";
}
else if (temp < 26) {
  recommendation = "Usa ropa cómoda";
}
else {
  recommendation = "Usa ropa ligera";
}

if (condition.includes("rain")) {
  recommendation += " y lleva paraguas";
}

return [{
  json:{
    city:$json.name,
    temperature:temp,
    condition:condition,
    recommendation
  }
}]
Nodo 4 — MySQL

Configurar conexión.

Query:

INSERT INTO weather_queries
(city, temperature, weather_condition, recommended_clothes)
VALUES
(
{{$json.city}},
{{$json.temperature}},
{{$json.condition}},
{{$json.recommendation}}
)
Nodo 5 — Respond to Webhook

Devuelve la respuesta.

{
 "city": {{$json.city}},
 "temperature": {{$json.temperature}},
 "condition": "{{$json.condition}}",
 "recommendation": "{{$json.recommendation}}"
}
7. Conectar Frontend con n8n

Cambiar en script.js:

fetch("http://localhost:5678/webhook/weather-outfit")

Body:

body: JSON.stringify({
 city: city
})
8. Crear el servidor MCP

Crear carpeta:

mcp-server

Instalar dependencias:

npm init -y
npm install express

Crear server.js.

Ejecutar:

node server.js

Probar endpoints:

POST /tool/get_weather
POST /tool/recommend_outfit
9. Integrar MCP con n8n

En lugar de llamar directo a la API meteorológica:

n8n → MCP Server

Flujo:

n8n
  ↓
MCP get_weather
  ↓
MCP recommend_outfit
  ↓
MySQL
10. Pruebas finales

Probar flujo completo:

Usuario escribe ciudad

Frontend envía solicitud

n8n recibe webhook

Consulta API clima

Genera recomendación

Guarda en MySQL

Devuelve respuesta

Resultado esperado:

Ciudad: Bogotá
Temperatura: 15°C
Condición: Nublado
Recomendación: Usa chaqueta ligera
11. Documentación

Crear carpeta:

docs/

Agregar:

architecture.md
api-spec.md
workflow-n8n.png
database-design.md
Flujo final del sistema
Usuario
   ↓
Frontend
   ↓
Backend API
   ↓
n8n (orquestador)
   ↓
API Clima / MCP
   ↓
Reglas de recomendación
   ↓
MySQL
   ↓
Respuesta al usuario

Si quieres, también puedo darte:

El workflow de n8n listo para importar (JSON)

El backend completo en Spring Boot ya estructurado

El MCP server real con integración a OpenWeather

Un diagrama de arquitectura profesional para tu documento (muy recomendado para el proyecto).