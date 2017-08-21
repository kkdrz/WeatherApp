package com.tieto.wro.java.a17.weather.controller;

import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import java.util.List;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.log4j.Log4j;

@Log4j
@Singleton
@Path("/")
public class RESTController extends WeatherController {

	public RESTController(List<City> supportedCities) {
		super(supportedCities);
		log.info("WundergroundController instantiated.");
	}

	@Override
	@GET
	@Path("/weather")
	@Produces(MediaType.APPLICATION_JSON)
	public List<CityWeather> getCitiesWeathers() {
		return super.getCitiesWeathers();
	}

	@Override
	@GET
	@Path("/weather/{city}")
	@Produces(MediaType.APPLICATION_JSON)
	public CityWeather getCityWeather(@PathParam("city") String cityName) {
		return super.getCityWeather(cityName);
	}

	@Override
	@GET
	@Path("/weather/update")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCache() {
		return super.updateCache();
	}

	@Override
	@GET
	@Path("/weather/cities")
	@Produces(MediaType.APPLICATION_JSON)
	public List<City> getSupportedCities() {
		return super.getSupportedCities();
	}

}
