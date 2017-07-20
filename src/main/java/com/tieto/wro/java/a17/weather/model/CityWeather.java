package com.tieto.wro.java.a17.weather.model;

import lombok.Data;

@Data
public class CityWeather {

    private String location;
    private Double temperatureCelsius;
    private String relativeHumidity;
    private String windDirection;
    private String weather;
    private String windString;
    private String weatherDate;

    public CityWeather() {}

    public CityWeather(String location, Double temperatureCelsius, String relativeHumidity, String windDirection, String weather, String windString, String weatherDate) {
        this.location = location;
        this.temperatureCelsius = temperatureCelsius;
        this.relativeHumidity = relativeHumidity;
        this.windDirection = windDirection;
        this.weather = weather;
        this.windString = windString;
        this.weatherDate = weatherDate;
    }
    
    
}
