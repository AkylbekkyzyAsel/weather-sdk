package test.kameleoon.weather.sdk;

import java.time.Duration;
import java.time.Instant;


class CacheEntry {

    private final WeatherResponse response;
    private final Instant createdAt;

    public CacheEntry(WeatherResponse response) {
        this.response = response;
        this.createdAt = Instant.now();
    }

    WeatherResponse getWeather() {
        return this.response;
    }

    boolean isExpired(Duration ttl) {
        return createdAt.isBefore(Instant.now().minus(ttl));
    }

}
