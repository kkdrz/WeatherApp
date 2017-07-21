package com.tieto.wro.java.a17.weather.service;

import com.tieto.wro.java.a17.weather.WundergroundResponseTransformer;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.wunderground.WundergroundPathBuilder;
import com.tieto.wro.java.a17.wunderground.client.WundergroundClient;
import com.tieto.wro.java.a17.wunderground.model.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class WeatherServiceImpl implements WeatherService {

    private final WundergroundClient client;
    private final WundergroundResponseTransformer transformer;
    private final WundergroundPathBuilder pathBuilder;

    public WeatherServiceImpl(WundergroundClient client, WundergroundResponseTransformer transformer, WundergroundPathBuilder pathBuilder) {
        this.client = client;
        this.transformer = transformer;
        this.pathBuilder = pathBuilder;
    }

    @Override
    public CityWeather getCityWeather(String city) {
        if(!supportedCities.contains(city)) return null;
        String path = pathBuilder.buildPath("", city);
        Response response = client.getWeather(path);
        if (response == null) return null;
        return transformer.transform(response);
    }

    @Override
    public List<CityWeather> getCitiesWeathers() {
       List<CityWeather> response = new ArrayList<>();
       for(String city : supportedCities) {
           response.add(getCityWeather(city));
       }
       return response;
    }
    
    private final List<String> supportedCities = Arrays.asList("Wroclaw", "Bialystok", "Czestochowa", "Bielsko-Biala", "Goleniow", "Kolobrzeg");
}
