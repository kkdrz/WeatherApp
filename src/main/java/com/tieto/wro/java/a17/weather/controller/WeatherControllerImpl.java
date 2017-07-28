package com.tieto.wro.java.a17.weather.controller;

import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.weather.service.WeatherServiceImpl;
import com.tieto.wro.java.a17.wunderground.client.WundergroundClient;
import java.util.List;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.log4j.Log4j;
import org.glassfish.jersey.server.ResourceConfig;

@Log4j
@Path("/weather")
@ApplicationPath("")
public class WeatherControllerImpl extends ResourceConfig {

	WeatherServiceImpl service;
	private static final String API_KEY = "b6bfc129d8a2c4ea";
	private static final String API_URL = "http://localhost:8089/api.wunderground.com/api/" + API_KEY;

	public WeatherControllerImpl() {
		packages("com.tieto.wro.java.a17.weather.controller");
		initWeatherService();
		log.info("WundergroundController instantiated.");
	}

	private void initWeatherService() {
		WundergroundClient client = new WundergroundClient(API_URL);
		service = new WeatherServiceImpl(client);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCitiesWeathers() {
		log.info("getCitiesWeathers request invoked.");
		List<CityWeather> entities = service.getCitiesWeathers();

		if (entities == null || entities.isEmpty()) {
			log.warn("None CityWeather was found.");
			return responseNotFound();
		}

		log.info("Mapping entities to JSON.");
		return responseOK(entities);
	}

	@GET
	@Path("/{city}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCityWeather(@PathParam("city") String city) {
		log.info("getCityWeather request for city: \"" + city + "\" invoked.");
		CityWeather entity = service.getCityWeather(city);

		if (entity == null) {
			log.warn("CityWeather for city: \"" + city + "\" couldn't be found - null.");
			return responseNotFound();
		}

		log.info("Mapping CityWeather entity to JSON.");
		return responseOK(entity);
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
