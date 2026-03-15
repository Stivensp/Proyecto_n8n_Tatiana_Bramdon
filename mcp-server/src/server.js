import express from "express";
import cors from "cors";
import { getWeather } from "./tools/getWeather.js";
import { recommendOutfit } from "./tools/recommendOutfit.js";

// Servidor MCP expone herramientas HTTP consumibles por el backend o clientes.
const app = express();
app.use(cors());
app.use(express.json());

// Healthcheck simple para validar disponibilidad.
app.get("/health", (req, res) => {
  res.json({ status: "ok" });
});

// Herramienta: obtiene clima basico para una ciudad.
app.post("/tool/get_weather", async (req, res) => {
  try {
    const { city } = req.body || {};
    const result = await getWeather(city);
    res.json(result);
  } catch (error) {
    res.status(400).json({ error: error.message || "Invalid request" });
  }
});

// Herramienta: recomienda outfit simple basado en temperatura/condicion.
app.post("/tool/recommend_outfit", (req, res) => {
  try {
    const { temperature, condition } = req.body || {};
    const result = recommendOutfit(temperature, condition);
    res.json(result);
  } catch (error) {
    res.status(400).json({ error: error.message || "Invalid request" });
  }
});

const port = process.env.PORT || 4000;
app.listen(port, () => {
  console.log(`MCP server listening on port ${port}`);
});
