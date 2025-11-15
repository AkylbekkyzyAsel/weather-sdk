package test.kameleoon.weather.sdk;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_NULL)
public final class WeatherResponse {

    private final WeatherCondition weather;
    private final Temperature temperature;
    private final Integer visibility;
    private final Wind wind;
    private final long datetime;
    private final Sys sys;
    private final Integer timezone;
    private final String name;

    WeatherResponse(WeatherCondition weather,
                           Temperature temperature,
                           Integer visibility,
                           Wind wind,
                           long datetime,
                           Sys sys,
                           Integer timezone,
                           String name) {
        this.weather = weather;
        this.temperature = temperature;
        this.visibility = visibility;
        this.wind = wind;
        this.datetime = datetime;
        this.sys = sys;
        this.timezone = timezone;
        this.name = name;
    }

    public WeatherCondition getWeather() {
        return weather;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public Wind getWind() {
        return wind;
    }

    public long getDatetime() {
        return datetime;
    }

    public Sys getSys() {
        return sys;
    }

    public Integer getTimezone() {
        return timezone;
    }

    public String getName() {
        return name;
    }


    public static final class WeatherCondition {
        private final String main;
        private final String description;

        WeatherCondition(String main, String description) {
            this.main = main;
            this.description = description;
        }

        public String getMain() {
            return main;
        }

        public String getDescription() {
            return description;
        }
    }

    public static final class Temperature {
        private final Double temp;
        @JsonProperty("feels_like")
        private final Double feelsLike;

        Temperature(Double temp, Double feelsLike) {
            this.temp = temp;
            this.feelsLike = feelsLike;
        }

        public Double getTemp() {
            return temp;
        }

        public Double getFeelsLike() {
            return feelsLike;
        }
    }

    public static final class Wind {
        private final Double speed;

        Wind(Double speed) {
            this.speed = speed;
        }

        public Double getSpeed() {
            return speed;
        }
    }

    public static final class Sys {
        private final Long sunrise;
        private final Long sunset;

        Sys(Long sunrise, Long sunset) {
            this.sunrise = sunrise;
            this.sunset = sunset;
        }

        public Long getSunrise() {
            return sunrise;
        }

        public Long getSunset() {
            return sunset;
        }
    }

    @Override
    public String toString() {
        return "WeatherResponse{name=" + name + ", temp=" + temperature + "}";
    }

}
