async function consultWeather() {

    const city = document.getElementById("cityInput").value;
  
    if (!city) {
      alert("Por favor ingresa una ciudad");
      return;
    }
  
    try {
  
      const response = await fetch("http://localhost:3001/api/weather-outfit", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({ city: city })
      });
  
      const data = await response.json();
  
      const payload = Array.isArray(data) ? data[0] : data;
      document.getElementById("city").textContent = payload.city || "-";
      document.getElementById("temperature").textContent =
        payload.temperature !== null && payload.temperature !== undefined
          ? payload.temperature + "°C"
          : "-";
      document.getElementById("condition").textContent = payload.condition || "-";
      document.getElementById("recommendation").textContent =
        payload.recommendation || "-";
  
      document.getElementById("result").classList.remove("hidden");
  
    } catch (error) {
  
      console.error(error);
      alert("Error al consultar el clima");
  
    }
  }
