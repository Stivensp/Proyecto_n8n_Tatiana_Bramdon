import express from "express";
import cors from "cors";
import { getWeather } from "./tools/getWeather.js";
import { recommendOutfit } from "./tools/recommendOutfit.js";

const app = express();
app.use(cors());
app.use(express.json());

app.get("/health", (req, res) => {
  res.json({ status: "ok" });
});

app.post("/tool/get_weather", async (req, res) => {
  try {
    const { city } = req.body || {};
    const result = await getWeather(city);
    res.json(result);
  } catch (error) {
    res.status(400).json({ error: error.message || "Invalid request" });
  }
});

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
