package com.tieto.wro.java.a17.weather.service;

import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.weather.provider.CityWeatherProvider;
import com.tieto.wro.java.a17.weather.provider.client.WundergroundClient;
import com.tieto.wro.java.a17.weather.provider.database.DbCache;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j;

@Log4j
public class WeatherServiceImpl {

	private CityWeatherProvider provider;
	private List<City> supportedCities;
	private final DbCache cache;

	public WeatherServiceImpl(List<City> supportedCities) {
		this(new WundergroundClient(), supportedCities, new DbCache(), true);
	}

	WeatherServiceImpl(
			CityWeatherProvider provider,
			List<City> supportedCities,
			DbCache cache,
			boolean cacheEnabled) {
		this.provider = provider;
		this.supportedCities = supportedCities;
		this.cache = cache;
		log.info("WeatherService instantiated with supported cities:\n" + supportedCities);
		if (cacheEnabled) {
			setCacheEnabled();
		}
	}

	private void setCacheEnabled() {
		updateCache();
		this.provider = cache;
	}

	public CityWeather getCityWeather(String cityName) {
		log.info("getCityWeather for \"" + cityName + "\" invoked.");
		City city = getCityIfSupported(cityName);
		return city != null ? getCityWeather(city) : null;
	}

	private City getCityIfSupported(String cityName) {
		City city = findSupportedCityByName(cityName);
		if (city == null) {
			log.warn("Requested city (" + cityName + ") is not supported.");
			return null;
		}
		log.info("Requested city is supported: " + city);
		return city;
	}

	private City findSupportedCityByName(String cityName) {
		cityName = cityName.toLowerCase();
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

	private CityWeather getCityWeather(City city) {
		return provider.getCityWeather(city);
	}

	public void updateCache() {
		log.info("Updating CityWeather cache.");
		List<CityWeather> cityWeathers = getCitiesWeathers();
		cityWeathers.forEach((cw) -> {
			cache.saveOrUpdate(cw);
		});
		log.info("Updating cache completed.");
	}

}
