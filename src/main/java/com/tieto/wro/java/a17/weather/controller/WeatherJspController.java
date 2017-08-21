package com.tieto.wro.java.a17.weather.controller;

import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import java.util.List;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import lombok.extern.log4j.Log4j;
import org.glassfish.jersey.server.mvc.Template;

@Log4j
@Singleton
@Path("/view/weather")
public class WeatherJspController extends WeatherController {
	
	public WeatherJspController(List<City> supportedCities) {
		super(supportedCities);
	}
	
	@GET
	@Template(name = "/index.jsp")
	public List<CityWeather> getAllCitiesWeathersSorted() {
		log.info("View all cityWeathers");
		List<CityWeather> citiesWeathers = super.getCitiesWeathers();
		
		citiesWeathers.sort((CityWeather o1, CityWeather o2) -> {
			return o1.getLocation().compareToIgnoreCase(o2.getLocation());
		});
		
		return citiesWeathers;
	}
	
	@GET
	@Path("/{city}")
	@Template(name = "/city.jsp")
	public CityWeather getCityWeather(@PathParam("city") String cityName) {
		return super.getCityWeather(cityName);
	}
}
