package test.kameleoon.weather.sdk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;


public class WeatherSdk {

    private final OpenWeatherProvider provider;
    private final WeatherCache cache;
    private final boolean pollingEnabled;
    private final WeatherPollingScheduler scheduler;
    private Duration ttl;

    WeatherSdk(OpenWeatherProvider provider, WeatherCache cache, boolean pollingEnabled, WeatherPollingScheduler scheduler) {
        this.provider = provider;
        this.cache = cache;
        this.pollingEnabled = pollingEnabled;
        this.scheduler = scheduler;
    }

    public WeatherResponse getWeather(String city) {
        if (city == null || city.isBlank()) {
            throw new WeatherSdkException("City name must not be empty");
        }

        String key = city.trim();
        var e = cache.get(key);
        if (e != null && !e.isExpired(ttl)) {
            return e.getWeather();
        }

        var weather = provider.getWeather(key);
        cache.put(key, new CacheEntry(weather));
        return weather;
    }

    public String getWeatherJson(String city) throws WeatherSdkException {
        WeatherResponse response = getWeather(city);
        try {
            return new ObjectMapper().writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new WeatherSdkException("Failed to serialize weather response to JSON", e);
        }
    }

    public void shutdown() {
        if (pollingEnabled && scheduler != null) {
            scheduler.stop();
        }
    }

    public void setUnits(WeatherUnit units) {
        provider.setUnits(units);
    }


    public void setPollInterval(long intervalMs) {
        scheduler.setInterval(intervalMs);
    }

    public void setCacheTtl(long ttlMs) {
        this.ttl = Duration.ofMillis(ttlMs);
        cache.setTtl(ttl);
    }

}
