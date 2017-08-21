package com.tieto.wro.java.a17.weather;

import com.tieto.wro.java.a17.weather.controller.JSPController;
import com.tieto.wro.java.a17.weather.controller.RESTController;
import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.provider.client.WundergroundClient;
import com.tieto.wro.java.a17.weather.provider.client.WundergroundResponseTransformer;
import com.tieto.wro.java.a17.weather.provider.database.DbCache;
import com.tieto.wro.java.a17.weather.service.WeatherService;
import com.tieto.wro.java.a17.weather.service.WeatherServiceCache;
import java.util.List;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.client.Client;
import lombok.extern.log4j.Log4j;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;

@Log4j
@ApplicationPath("")
public class App extends ResourceConfig {

	private static List<City> SUPPORTED_CITIES;
	private static final String CITIES_JSON_PATH = "src/main/resources/cities.json";
	private static final String API_URL = "http://localhost:8089/api.wunderground.com/api/b6bfc129d8a2c4ea";

	public App() {
		loadSupportedCities();

		packages("com.tieto.wro.java.a17.weather.controller");
		register(JspMvcFeature.class);
		property(JspMvcFeature.TEMPLATE_BASE_PATH, "/WEB-INF/jsp");
		register(new DependencyBinder());
		register(new RESTController(SUPPORTED_CITIES, API_URL));
		register(new JSPController(SUPPORTED_CITIES, API_URL));

	}

	private void loadSupportedCities() {
		log.info("Loading supported cities.");
		SUPPORTED_CITIES = new SupportedCitiesProvider().getSupportedCities(CITIES_JSON_PATH);
		if (SUPPORTED_CITIES == null || SUPPORTED_CITIES.isEmpty()) {
			log.error("There is no cities in SUPPORTED_CITY. Check if path (" + CITIES_JSON_PATH + ") is correct and if JSON file exists.");
		}
		log.info("Loading supported cities done.");
	}

	public class DependencyBinder extends AbstractBinder {

		@Override
		protected void configure() {
			bind(SUPPORTED_CITIES).to(List.class).named("SUPPORTED_CITIES");
			bind(API_URL).to(String.class).named("API_URL");

			bind(DbCache.class).to(DbCache.class);
			
			bind(Client.class).to(Client.class);
			bind(WundergroundResponseTransformer.class).to(WundergroundResponseTransformer.class);

			WundergroundClient client = new WundergroundClient(API_URL);
			bind(client).to(WundergroundClient.class);

			WeatherServiceCache service = new WeatherServiceCache(new DbCache(), SUPPORTED_CITIES, client);
			bind(service).to(WeatherService.class);
		}
	}

}
