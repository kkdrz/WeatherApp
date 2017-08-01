package com.tieto.wro.java.a17.weather;

import com.tieto.wro.java.a17.weather.model.City;
import java.util.List;
import javax.ws.rs.ApplicationPath;
import lombok.extern.log4j.Log4j;
import org.glassfish.jersey.server.ResourceConfig;

@Log4j
@ApplicationPath("")
public class App extends ResourceConfig {
	
	public static List<City> SUPPORTED_CITIES;
	private static final String CITIES_JSON_PATH = "src/main/resources/cities.json";
	
	public App() {
		packages("com.tieto.wro.java.a17.weather.controller");
		SUPPORTED_CITIES = new SupportedCitiesProvider().getSupportedCities(CITIES_JSON_PATH);
		if (SUPPORTED_CITIES == null || SUPPORTED_CITIES.isEmpty()) {
			log.error("There is no cities in SUPPORTED_CITY. Check if path is correct and if JSON file exists.");
		}
	}
	
}
