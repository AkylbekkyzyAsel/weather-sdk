package test.kameleoon.weather.sdk;

import java.time.Duration;
import java.util.*;


class WeatherCache {

    private final int capacity;
    private Duration ttl;
    private final LinkedHashMap<String, CacheEntry> map;

    WeatherCache(int capacity, Duration ttl) {
        this.capacity = Math.max(1, capacity);
        this.ttl = ttl;
        this.map = new LinkedHashMap<>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, CacheEntry> eldest) {
                return size() > WeatherCache.this.capacity;
            }
        };
    }

    synchronized CacheEntry get(String city) {
        var key = city.toLowerCase();
        var e = map.get(key);
        if (e == null) return null;
        if (e.isExpired(ttl)) {
            map.remove(key);
            return null;
        }
        return e;
    }

    synchronized void put(String city, CacheEntry entry) {
        map.put(city.toLowerCase(), entry);
    }

    synchronized List<String> getAllCities() {
        return new ArrayList<>(map.keySet());
    }

    void setTtl(Duration ttl) {
        this.ttl = ttl;
    }

}
