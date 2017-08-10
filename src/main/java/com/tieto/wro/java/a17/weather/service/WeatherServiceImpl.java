package com.tieto.wro.java.a17.weather.service;

import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.weather.provider.CityWeatherProvider;
import com.tieto.wro.java.a17.weather.provider.database.DbCache;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.NotFoundException;
import lombok.extern.log4j.Log4j;

@Log4j
public class WeatherServiceImpl {

	private CityWeatherProvider provider;
	private CityWeatherProvider updateProvider;
	private List<City> supportedCities;
	private DbCache cache;

	public WeatherServiceImpl(List<City> supportedCities, CityWeatherProvider provider) {
		this(supportedCities, provider, null);
	}

	public WeatherServiceImpl(List<City> supportedCities, CityWeatherProvider provider, DbCache cache) {
		this.provider = provider;
		this.supportedCities = supportedCities;

		if (supportedCities.isEmpty()) {
			log.error("The list of supported cities is empty.");
		}

		enableCacheIfNotNull(cache);
		log.info("WeatherService instantiated with supported cities:\n" + supportedCities);
	}

	private void enableCacheIfNotNull(DbCache cache) {
		if (cache != null) {
			this.cache = cache;
			this.updateProvider = this.provider;
			this.provider = cache;
			updateCache();
			log.info("Cache is enabled.");
		}
	}

	public CityWeather getCityWeather(String cityName) throws IllegalArgumentException {
		City city = getCityIfSupported(cityName);
		return getCityWeather(city);
	}

	private City getCityIfSupported(String cityName) throws IllegalArgumentException {
		cityName = cityName.toLowerCase();
		for (City c : supportedCities) {
			if (cityName.equals(c.getName())) {
				return c;
			}
		}
		throw new IllegalArgumentException("Requested city \"" + cityName + "\" is not supported.");
	}

	private CityWeather getCityWeather(City city) {
		return getCityWeather(city, provider);
	}

	private CityWeather getCityWeather(City city, CityWeatherProvider provider) {
		try {
			return provider.getCityWeather(city);
		} catch (NotFoundException e) {
			log.error("Requested city: " + city + "not found by provider: " + provider, e);
			throw e;
		}
	}

	public List<CityWeather> getCitiesWeathers() {
		return getCitiesWeathers(provider);
	}

	private List<CityWeather> getCitiesWeathers(CityWeatherProvider provider) {
		List<CityWeather> response = new ArrayList<>();
		for (City city : supportedCities) {
			response.add(getCityWeather(city, provider));
		}
		return response;
	}

	public void updateCache() {
		if (updateProvider == null) {
			throw new UnsupportedOperationException("UpdateProvider is null. Cache is probably not enabled.");
		} else {
			log.info("Updating CityWeather cache.");
			List<CityWeather> cityWeathers = getCitiesWeathers(updateProvider);
			cityWeathers.forEach((cw) -> {
				cache.saveOrUpdate(cw);
			});
			log.info("Updating cache completed.");
		}
	}

}
