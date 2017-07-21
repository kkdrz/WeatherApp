package com.tieto.wro.java.a17.weather.service;

import com.tieto.wro.java.a17.weather.model.CityWeather;
import java.util.List;


public interface WeatherService {

    public CityWeather getCityWeather(String city);
    public List<CityWeather> getCitiesWeathers();
    
}
