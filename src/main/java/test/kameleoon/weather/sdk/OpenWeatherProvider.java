package test.kameleoon.weather.sdk;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static test.kameleoon.weather.sdk.WeatherUnit.STANDARD;


class OpenWeatherProvider implements WeatherProvider {

    private final String apiKey;
    private final String baseUrl;
    private final SimpleHttpClient client;
    private WeatherUnit units;
    private static final String WEATHER_PATH = "/data/2.5/weather?q=%s&appid=%s&units=%s";

    OpenWeatherProvider(String apiKey, String baseUrl, SimpleHttpClient client) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.client = client;
        this.units = STANDARD;
    }

    @Override
    public WeatherResponse getWeather(String city) {
        try {
            String q = URLEncoder.encode(city, StandardCharsets.UTF_8);
            String url = String.join("", baseUrl,
                    String.format(WEATHER_PATH, q, apiKey, URLEncoder.encode(units.name(), StandardCharsets.UTF_8)));
            String json = client.get(url);

            return WeatherParser.parse(json);
        } catch (WeatherSdkException e) {
            throw e;
        } catch (Exception e) {
            throw new WeatherSdkException("OpenWeatherProvider error: " + e.getMessage(), e);
        }
    }

    void setUnits(WeatherUnit units) {
        this.units = units;
    }

}
