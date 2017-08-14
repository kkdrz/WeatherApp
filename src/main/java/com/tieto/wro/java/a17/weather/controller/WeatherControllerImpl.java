package com.tieto.wro.java.a17.weather.controller;

import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.weather.provider.client.WundergroundClient;
import com.tieto.wro.java.a17.weather.provider.database.DbCache;
import com.tieto.wro.java.a17.weather.service.WeatherService;
import com.tieto.wro.java.a17.weather.service.WeatherServiceCache;
import java.util.List;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.log4j.Log4j;

@Log4j
@Singleton
@Path("/weather")
public class WeatherControllerImpl {

	private final String WUNDER_URL = "http://api.wunderground.com/api/b6bfc129d8a2c4ea";
	private final List<City> supportedCities;
	private WeatherService service;

	public WeatherControllerImpl(List<City> supportedCities) {
		this.supportedCities = supportedCities;
		initWeatherService();
		log.info("WundergroundController instantiated.");
	}

	private void initWeatherService() {
		WundergroundClient client = new WundergroundClient(WUNDER_URL);
		//service = new WeatherServiceImpl(supportedCities, client);
		service = new WeatherServiceCache(new DbCache(), supportedCities, client);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<CityWeather> getCitiesWeathers() {
		log.info("getCitiesWeathers request invoked.");
		List<CityWeather> entities = service.getCitiesWeathers();
		return entities.isEmpty() ? null : entities;
	}

	@GET
	@Path("/{city}")
	@Produces(MediaType.APPLICATION_JSON)
	public CityWeather getCityWeather(@PathParam("city") String cityName) {
		log.info("getCityWeather request for city: \"" + cityName + "\" invoked.");
		try {
			City city = getCityIfSupported(cityName.toLowerCase());
			CityWeather entity = service.getCityWeather(city);
			return entity;
		} catch (NotFoundException | IllegalArgumentException e) {
			log.warn("CityWeather for city: \"" + cityName + "\" couldn't be found.", e);
			return null;
		}
	}

	@GET
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCache() {
		try {
			log.info("Update cache request invoked.");
			service.updateCache();
			return null;
		} catch (UnsupportedOperationException e) {
			log.error("Cache is not enabled.", e);
			return responseNoCache();
		}
	}

	private City getCityIfSupported(String cityName) {
		cityName = cityName.toLowerCase();
		for (City c : supportedCities) {
			if (cityName.equals(c.getName())) {
				return c;
			}
		}
		throw new IllegalArgumentException("Requested city \"" + cityName + "\" is not supported.");
	}

	private Response responseOK(Object entity) {
		return Response.status(Response.Status.OK)
				.entity(entity)
				.build();
	}

	private Response responseNotFound() {
		return Response.status(Response.Status.NOT_FOUND)
				.entity("No results could be found.")
				.build();
	}

	private Response responseNoCache() {
		return Response.status(Response.Status.NOT_IMPLEMENTED)
				.entity("Cache is not enabled.")
				.build();
	}
}
