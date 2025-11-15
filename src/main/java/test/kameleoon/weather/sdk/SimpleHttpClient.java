package test.kameleoon.weather.sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;


class SimpleHttpClient {

    private static final Logger log = LoggerFactory.getLogger(SimpleHttpClient.class);
    private final HttpClient client;
    private final int httpMaxRetries;
    private final Duration httpTimeout;
    private final int httpBackoffBaseMs;

    SimpleHttpClient() {
        this(Duration.ofSeconds(10), 2);
    }

    SimpleHttpClient(Duration timeout, int maxRetries) {
        this(timeout, maxRetries, 200);
    }

    SimpleHttpClient(Duration httpTimeout, int httpMaxRetries, int httpBackoffBaseMs) {
        this.client = HttpClient.newBuilder()
                .connectTimeout(httpTimeout)
                .build();
        this.httpMaxRetries = Math.max(0, httpMaxRetries);
        this.httpTimeout = httpTimeout;
        this.httpBackoffBaseMs = httpBackoffBaseMs;
    }

    String get(String url) {
        int attempt = 0;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(httpTimeout)
                .GET()
                .build();

        while (true) {
            try {
                attempt++;
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                int code = response.statusCode();
                if (code >= 200 && code < 300) {
                    return response.body();
                } else {
                    String body = response.body();
                    throw new WeatherSdkException("HTTP error: " + code + " body: " + body);
                }
            } catch (IOException | InterruptedException e) {
                if (attempt > httpMaxRetries) {
                    throw new WeatherSdkException("HTTP GET failed after retries: " + e.getMessage(), e);
                }
                int sleep = httpBackoffBaseMs * (1 << (attempt - 1));
                log.warn("HTTP GET failed (attempt {}), will retry after {}ms: {}", attempt, sleep, e.toString());
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                    throw new WeatherSdkException("Interrupted during HTTP backoff", ignored);
                }
            }
        }
    }

}
