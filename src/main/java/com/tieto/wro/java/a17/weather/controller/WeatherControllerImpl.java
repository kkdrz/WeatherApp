package com.tieto.wro.java.a17.weather.controller;

import com.tieto.wro.java.a17.weather.App;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.weather.provider.client.WundergroundClient;
import com.tieto.wro.java.a17.weather.provider.database.DbCache;
import com.tieto.wro.java.a17.weather.service.WeatherServiceImpl;
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
@Path("/weather")
public class WeatherControllerImpl {

	private WeatherServiceImpl service;

	public WeatherControllerImpl() {
		initWeatherService();
		log.info("WundergroundController instantiated.");
	}

	private void initWeatherService() {
		service = new WeatherServiceImpl(App.SUPPORTED_CITIES, new WundergroundClient(), new DbCache());
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCitiesWeathers() {
		log.info("getCitiesWeathers request invoked.");
		try {
			List<CityWeather> entities = service.getCitiesWeathers();
			return responseOK(entities);
		} catch (IllegalArgumentException e) {
			log.warn("None CityWeather was found.", e);
			return responseNotFound();
		}
	}

	@GET
	@Path("/{city}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCityWeather(@PathParam("city") String city) {
		log.info("getCityWeather request for city: \"" + city + "\" invoked.");
		try {
			CityWeather entity = service.getCityWeather(city);
			return responseOK(entity);
		} catch (IllegalArgumentException e) {
			log.warn("CityWeather for city: \"" + city + "\" couldn't be found - null.", e);
			return responseNotFound();
		}
	}

	@GET
	@Path("/update")
	public void updateCache() {
		log.info("Update cache request invoked.");
		service.updateCache();
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
}
