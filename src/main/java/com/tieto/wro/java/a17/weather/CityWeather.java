package com.tieto.wro.java.a17.weather;

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
    
}
