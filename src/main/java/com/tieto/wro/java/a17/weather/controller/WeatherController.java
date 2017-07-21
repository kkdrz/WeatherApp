package com.tieto.wro.java.a17.weather.controller;

import javax.ws.rs.core.Response;


public interface WeatherController {
    
    public Response getCitiesWeathers();
    public Response getCityWeather(String city);
}
