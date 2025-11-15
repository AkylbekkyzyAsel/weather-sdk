package test.kameleoon.weather.sdk;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


public class WeatherSdkRegistry {

    public static final Map<String, WeatherSdk> INSTANCES = new HashMap<>();
    private static final String OPEN_WEATHER_BASE_URL = "https://api.openweathermap.org";
    private static final int CACHE_SIZE = 10;
    private static final Duration DEFAULT_CACHE_TTL = Duration.ofMinutes(10);
    private static final long DEFAULT_POLL_INTERVAL_MILLIS = 300_000;

    public static WeatherSdk getInstance(String apiKey, SdkMode mode) {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new WeatherSdkException("API key must not be empty");
        }

        var existing = INSTANCES.get(apiKey);
        if (existing != null) {
            return existing;
        }
        var httpClient = new SimpleHttpClient();

        var provider = new OpenWeatherProvider(apiKey, OPEN_WEATHER_BASE_URL, httpClient);
        var cache = new WeatherCache(CACHE_SIZE, DEFAULT_CACHE_TTL);

        WeatherPollingScheduler scheduler = null;
        boolean polling = mode == SdkMode.POLLING;
        if (polling) {
            scheduler = new WeatherPollingScheduler(provider, cache, DEFAULT_POLL_INTERVAL_MILLIS);
            scheduler.start();
        }

        var sdk = new WeatherSdk(provider, cache, polling, scheduler);
        INSTANCES.put(apiKey, sdk);
        return sdk;
    }

    public static void deleteInstance(String apiKey) {
        var sdk = INSTANCES.remove(apiKey);
        if (sdk != null) {
            sdk.shutdown();
        }
    }

}
