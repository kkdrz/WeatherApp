package com.tieto.wro.java.a17.weather.provider;

import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import javax.ws.rs.NotFoundException;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface CityWeatherProvider {

	public CityWeather getCityWeather(City city) throws NotFoundException;

}
