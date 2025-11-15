package test.kameleoon.weather.sdk;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


class WeatherParser {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private WeatherParser() {
    }

    static WeatherResponse parse(String json) {
        try {
            JsonNode root = MAPPER.readTree(json);

            // weather array first item
            JsonNode weatherNode = root.path("weather");
            String main = safeText(weatherNode, 0, "main");
            String description = safeText(weatherNode, 0, "description");

            JsonNode mainNode = root.path("main");
            double temp = mainNode.path("temp").asDouble(Double.NaN);
            double feelsLike = mainNode.path("feels_like").asDouble(Double.NaN);

            int visibility = root.path("visibility").asInt(0);

            JsonNode windNode = root.path("wind");
            double windSpeed = windNode.path("speed").asDouble(0.0);

            long dt = root.path("dt").asLong(0L);

            JsonNode sysNode = root.path("sys");
            long sunrise = sysNode.path("sunrise").asLong(0L);
            long sunset = sysNode.path("sunset").asLong(0L);

            int timezone = root.path("timezone").asInt(0);
            String name = root.path("name").asText("");

            WeatherResponse.WeatherCondition weatherCond = new WeatherResponse.WeatherCondition(main, description);
            WeatherResponse.Temperature tempObj = new WeatherResponse.Temperature(temp, feelsLike);
            WeatherResponse.Wind wind = new WeatherResponse.Wind(windSpeed);
            WeatherResponse.Sys sys = new WeatherResponse.Sys(sunrise, sunset);

            return new WeatherResponse(weatherCond, tempObj, visibility, wind, dt, sys, timezone, name);

        } catch (Exception e) {
            throw new WeatherSdkException("Failed to parse weather JSON: " + e.getMessage(), e);
        }
    }

    private static String safeText(JsonNode arr, int idx, String field) {
        if (arr.isArray() && arr.size() > idx) {
            return arr.get(idx).path(field).asText("");
        }
        return "";
    }

}
