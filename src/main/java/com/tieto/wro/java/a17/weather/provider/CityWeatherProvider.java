package com.tieto.wro.java.a17.weather.provider;

import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.model.CityWeather;

public interface CityWeatherProvider {

	public CityWeather getCityWeather(City city);

}
