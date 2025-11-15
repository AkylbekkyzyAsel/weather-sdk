package test.kameleoon.weather.sdk;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


class WeatherPollingScheduler {

    private final WeatherProvider provider;
    private final WeatherCache cache;
    private ScheduledExecutorService executor;
    private long intervalMs;

    WeatherPollingScheduler(OpenWeatherProvider provider,
                                   WeatherCache cache,
                                   long intervalMs) {
        this.provider = provider;
        this.cache = cache;
        this.intervalMs = intervalMs;
    }

    synchronized void start() {
        if (executor != null && !executor.isShutdown()) return;
        executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "weather-sdk-poller");
            t.setDaemon(true);
            return t;
        });
        executor.scheduleAtFixedRate(this::refreshAll, 0, intervalMs, TimeUnit.MILLISECONDS);
    }

    synchronized void stop() {
        if (executor != null) {
            executor.shutdownNow();
            executor = null;
        }
    }

    void setInterval(long intervalMs) {
        this.intervalMs = intervalMs;
    }

    private void refreshAll() {
        try {
            List<String> cities = cache.getAllCities();
            for (String city : cities) {
                try {
                    var fresh = provider.getWeather(city);
                    cache.put(city, new CacheEntry(fresh));
                } catch (Exception _) {
                }
            }
        } catch (Throwable _) {}
    }

}
