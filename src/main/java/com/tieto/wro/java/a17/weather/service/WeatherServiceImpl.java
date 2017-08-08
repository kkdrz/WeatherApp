package com.tieto.wro.java.a17.weather.service;

import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.weather.provider.CityWeatherProvider;
import com.tieto.wro.java.a17.weather.provider.client.WundergroundClient;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j;

@Log4j
public class WeatherServiceImpl {
	
	private final CityWeatherProvider provider;
	private List<City> supportedCities;
	
	public WeatherServiceImpl(List<City> supportedCities) {
		this(new WundergroundClient(), supportedCities);
	}
	
	public WeatherServiceImpl(
			CityWeatherProvider provider, List<City> supportedCities) {
		this.provider = provider;
		this.supportedCities = supportedCities;
		log.info("WeatherService instantiated with supported cities:\n" + supportedCities);
	}
	
	public CityWeather getCityWeather(String cityName) {
		log.info("getCityWeather for \"" + cityName + "\" invoked.");
		City city = getCityIfSupported(cityName);
		return city != null ? getCityWeather(city) : null;
	}
	
	private CityWeather getCityWeather(City city) {
		return provider.getCityWeather(city);
	}
	
	private City getCityIfSupported(String cityName) {
		City city = findSupportedCityByName(cityName);
		if (city == null) {
			log.warn("Requested city (" + cityName + ") is not supported.");
			return null;
		}
		log.info("Requested city is supported: " + city.getName());
		return city;
	}
	
	private City findSupportedCityByName(String cityName) {
		for (City c : supportedCities) {
			if (cityName.equals(c.getName())) {
				return c;
			}
		}
		return null;
	}
	
	public List<CityWeather> getCitiesWeathers() {
		log.info("getCitiesWeathers method invoked.");
		List<CityWeather> response = new ArrayList<>();
		for (City city : supportedCities) {
			response.add(getCityWeather(city));
		}
		log.info("Returning response: List<CityWeather>");
		return response;
	}
	
}
