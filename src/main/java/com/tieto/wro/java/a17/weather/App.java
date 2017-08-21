package com.tieto.wro.java.a17.weather;

import com.tieto.wro.java.a17.weather.controller.RESTController;
import com.tieto.wro.java.a17.weather.controller.JSPController;
import com.tieto.wro.java.a17.weather.model.City;
import java.util.List;
import javax.ws.rs.ApplicationPath;
import lombok.extern.log4j.Log4j;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;

@Log4j
@ApplicationPath("")
public class App extends ResourceConfig {

	public static List<City> SUPPORTED_CITIES;
	private static final String CITIES_JSON_PATH = "src/main/resources/cities.json";

	public App() {
		loadSupportedCities();
		packages("com.tieto.wro.java.a17.weather.controller");
		register(JspMvcFeature.class);
		property(JspMvcFeature.TEMPLATE_BASE_PATH, "/WEB-INF/jsp");
		register(new RESTController(SUPPORTED_CITIES));
		register(new JSPController(SUPPORTED_CITIES));

	}

	private void loadSupportedCities() {
		log.info("Loading supported cities.");
		SUPPORTED_CITIES = new SupportedCitiesProvider().getSupportedCities(CITIES_JSON_PATH);
		if (SUPPORTED_CITIES == null || SUPPORTED_CITIES.isEmpty()) {
			log.error("There is no cities in SUPPORTED_CITY. Check if path (" + CITIES_JSON_PATH + ") is correct and if JSON file exists.");
		}
		log.info("Loading supported cities done.");
	}

}
