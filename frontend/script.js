async function consultWeather() {

    const city = document.getElementById("cityInput").value;
    const errorEl = document.getElementById("error");
    const loaderEl = document.getElementById("loader");
    errorEl.classList.add("hidden");
    errorEl.textContent = "";
    loaderEl.classList.remove("hidden");
  
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
  
      if (!response.ok) {
        throw new Error("No se encontró la ciudad");
      }

      const data = await response.json();
  
      const payload = Array.isArray(data) ? data[0] : data;
      if (!payload || !payload.city) {
        throw new Error("No se encontró la ciudad");
      }
      document.getElementById("city").textContent = payload.city || "-";
      document.getElementById("temperature").textContent =
        payload.temperature !== null && payload.temperature !== undefined
          ? payload.temperature + "°C"
          : "-";
      document.getElementById("condition").textContent = payload.condition || "-";
      document.getElementById("humidity").textContent =
        payload.humidity !== null && payload.humidity !== undefined
          ? payload.humidity + "%"
          : "-";
      document.getElementById("wind").textContent =
        payload.wind !== null && payload.wind !== undefined
          ? payload.wind + " km/h"
          : "-";
      document.getElementById("feelslike").textContent =
        payload.feels_like !== null && payload.feels_like !== undefined
          ? payload.feels_like + "°C"
          : "-";
      const recommendationEl = document.getElementById("recommendation");
      const recommendationText = payload.recommendation || "-";
      recommendationEl.innerHTML = renderRecommendation(recommendationText);
      updateHistory(payload);
  
      document.getElementById("result").classList.remove("hidden");
  
    } catch (error) {
  
      console.error(error);
      const message = error && error.message ? error.message : "Error al consultar el clima";
      errorEl.textContent = message;
      errorEl.classList.remove("hidden");
      document.getElementById("result").classList.add("hidden");
  
    } finally {
      loaderEl.classList.add("hidden");
    }
  }

function renderRecommendation(text) {
  if (!text) {
    return "-";
  }

  const lines = String(text).split(/\r?\n/);
  const htmlParts = [];
  let inList = false;

  for (const rawLine of lines) {
    const line = rawLine.trimEnd();

    if (line.trim() === "") {
      if (inList) {
        htmlParts.push("</ul>");
        inList = false;
      }
      htmlParts.push("<br>");
      continue;
    }

    const isBullet = line.trimStart().startsWith("* ");
    const content = isBullet ? line.trimStart().slice(2) : line;
    const safeContent = applyBold(escapeHtml(content));

    if (isBullet) {
      if (!inList) {
        htmlParts.push("<ul>");
        inList = true;
      }
      htmlParts.push(`<li>${safeContent}</li>`);
    } else {
      if (inList) {
        htmlParts.push("</ul>");
        inList = false;
      }
      htmlParts.push(`<p>${safeContent}</p>`);
    }
  }

  if (inList) {
    htmlParts.push("</ul>");
  }

  return htmlParts.join("");
}

function escapeHtml(value) {
  return String(value)
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#39;");
}

function applyBold(value) {
  return value.replace(/\*\*(.+?)\*\*/g, "<strong>$1</strong>");
}

function updateHistory(payload) {
  if (!payload || !payload.city) {
    return;
  }

  const history = loadHistory();
  const item = {
    city: payload.city,
    temperature: payload.temperature,
    condition: payload.condition
  };

  const filtered = history.filter((entry) => entry.city !== item.city);
  const next = [item, ...filtered].slice(0, 6);
  saveHistory(next);
  renderHistory(next);
}

function loadHistory() {
  try {
    const raw = localStorage.getItem("weatherHistory");
    if (!raw) {
      return [];
    }
    const parsed = JSON.parse(raw);
    return Array.isArray(parsed) ? parsed : [];
  } catch (error) {
    return [];
  }
}

function saveHistory(history) {
  localStorage.setItem("weatherHistory", JSON.stringify(history));
}

function renderHistory(history) {
  const historyEl = document.getElementById("history");
  if (!historyEl) {
    return;
  }
  historyEl.innerHTML = "";

  if (!history || history.length === 0) {
    historyEl.innerHTML = "<div class=\"history-empty\">Sin consultas a\u00fan.</div>";
    return;
  }

  history.forEach((entry) => {
    const itemEl = document.createElement("div");
    itemEl.className = "history-item";

    const cityEl = document.createElement("span");
    cityEl.textContent = entry.city || "-";

    const tempEl = document.createElement("span");
    tempEl.textContent =
      entry.temperature !== null && entry.temperature !== undefined
        ? entry.temperature + "°C"
        : "-";

    itemEl.appendChild(cityEl);
    itemEl.appendChild(tempEl);
    historyEl.appendChild(itemEl);
  });
}

document.addEventListener("DOMContentLoaded", () => {
  renderHistory(loadHistory());
});
