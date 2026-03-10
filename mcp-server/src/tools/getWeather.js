import fetch from "node-fetch";

const GEOCODING_URL = "https://geocoding-api.open-meteo.com/v1/search";
const WEATHER_URL = "https://api.open-meteo.com/v1/forecast";

function mapWeatherCode(code) {
  if (code === 0) return "despejado";
  if (code >= 1 && code <= 3) return "parcialmente nublado";
  if (code === 45 || code === 48) return "niebla";
  if (code >= 51 && code <= 57) return "llovizna";
  if (code >= 61 && code <= 67) return "lluvia";
  if (code >= 71 && code <= 77) return "nieve";
  if (code >= 80 && code <= 82) return "chubascos";
  if (code >= 85 && code <= 86) return "nevadas";
  if (code >= 95 && code <= 99) return "tormenta";
  return "condicion desconocida";
}

export async function getWeather(city) {
  if (!city) {
    throw new Error("city is required");
  }

  const geoResp = await fetch(`${GEOCODING_URL}?name=${encodeURIComponent(city)}&count=1&language=es&format=json`);
  if (!geoResp.ok) {
    throw new Error("Failed to fetch geocoding data");
  }
  const geoData = await geoResp.json();
  if (!geoData.results || geoData.results.length === 0) {
    throw new Error("City not found");
  }

  const { latitude, longitude, name } = geoData.results[0];

  const weatherResp = await fetch(
    `${WEATHER_URL}?latitude=${latitude}&longitude=${longitude}&current=temperature_2m,weather_code&timezone=auto`
  );
  if (!weatherResp.ok) {
    throw new Error("Failed to fetch weather data");
  }
  const weatherData = await weatherResp.json();

  const temperature = weatherData.current?.temperature_2m;
  const code = weatherData.current?.weather_code;

  return {
    city: name || city,
    temperature,
    condition: mapWeatherCode(code)
  };
}
