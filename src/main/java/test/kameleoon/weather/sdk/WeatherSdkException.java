package test.kameleoon.weather.sdk;


public class WeatherSdkException extends RuntimeException {

    public WeatherSdkException(String message) {
        super(message);
    }

    public WeatherSdkException(String message, Throwable cause) {
        super(message, cause);
    }

}