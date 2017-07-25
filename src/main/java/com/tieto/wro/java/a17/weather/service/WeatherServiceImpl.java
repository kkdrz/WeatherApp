package com.tieto.wro.java.a17.weather.service;

import com.tieto.wro.java.a17.weather.WundergroundResponseTransformer;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.wunderground.WundergroundPathBuilder;
import com.tieto.wro.java.a17.wunderground.client.WundergroundClient;
import com.tieto.wro.java.a17.wunderground.model.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.log4j.Log4j;

@Log4j
public class WeatherServiceImpl {

    private final WundergroundClient client;
    private final WundergroundResponseTransformer transformer;
    private final WundergroundPathBuilder pathBuilder;

    public WeatherServiceImpl(WundergroundClient client) {
        this(client, new WundergroundResponseTransformer(), new WundergroundPathBuilder());
    }

    public WeatherServiceImpl(WundergroundClient client, WundergroundResponseTransformer transformer, WundergroundPathBuilder pathBuilder) {
        this.client = client;
        this.transformer = transformer;
        this.pathBuilder = pathBuilder;
        log.info("WeatherService instantiated.");
    }

    public CityWeather getCityWeather(String city) {
        log.info("getCityWeather for \"" + city + "\" invoked.");
        city = city.toLowerCase();
        if (!isSupported(city)) {
            return null;
        }
        String path = pathBuilder.buildPath("", city);
        log.info("Getting Response from WundergroundClient with path: " + path);
        Response response = client.getWeather(path);
        if (response == null) {
            log.warn("Response from client is null. Requested path: " + path);
            return null;
        }
        return transformer.transform(response);
    }

    private boolean isSupported(String city) {
        if (!supportedCities.contains(city)) {
            log.warn("Requested city (" + city + ") is not supported.");
            return false;
        }
        return true;
    }

    public List<CityWeather> getCitiesWeathers() {
        log.info("getCitiesWeathers method invoked.");
        List<CityWeather> response = new ArrayList<>();
        for (String city : supportedCities) {
            response.add(getCityWeather(city));
        }
        log.info("Returning response: List<CityWeather>");
        return response;
    }

    private final List<String> supportedCities = Arrays.asList("wroclaw", "bialystok", "czestochowa", "bielsko-biala", "goleniow", "kolobrzeg");
}
