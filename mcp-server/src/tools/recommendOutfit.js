// Regla simple para recomendar outfit basado en temperatura y condicion.
export function recommendOutfit(temperature, condition) {
  if (temperature === null || temperature === undefined) {
    throw new Error("temperature is required");
  }

  let recommendation = "";

  if (temperature < 10) {
    recommendation = "abrigo grueso";
  } else if (temperature >= 10 && temperature <= 17) {
    recommendation = "chaqueta ligera";
  } else if (temperature >= 18 && temperature <= 25) {
    recommendation = "ropa comoda";
  } else {
    recommendation = "ropa ligera";
  }

  const normalizedCondition = (condition || "").toLowerCase();
  if (normalizedCondition.includes("lluvia") || normalizedCondition.includes("rain")) {
    recommendation += " y paraguas";
  }

  return { recommendation };
}
