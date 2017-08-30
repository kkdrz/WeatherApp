package com.tieto.wro.java.a17.weather;

import com.tieto.wro.java.a17.weather.controller.JSPController;
import com.tieto.wro.java.a17.weather.controller.RESTController;
import com.tieto.wro.java.a17.weather.provider.client.WundergroundClient;
import com.tieto.wro.java.a17.weather.provider.database.DbCache;
import com.tieto.wro.java.a17.weather.service.WeatherService;
import com.tieto.wro.java.a17.weather.service.WeatherServiceCache;
import com.tieto.wro.java.a17.weather.service.WeatherServiceImpl;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.ApplicationPath;
import lombok.extern.log4j.Log4j;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;

@Log4j
@ApplicationPath("")
public class App extends ResourceConfig {

	private static final String CITIES_JSON_PATH = "src/main/resources/cities.json";
	private static final String API_URL = "http://localhost:8089/api.wunderground.com/api/b6bfc129d8a2c4ea";

	public App() {
		packages("com.tieto.wro.java.a17.weather.controller", "com.tieto.wro.java.a17.weather.service");
		register(JspMvcFeature.class);
		property(JspMvcFeature.TEMPLATE_BASE_PATH, "/WEB-INF/jsp");
		register(new DependencyBinder());
		register(new RESTController(API_URL));
		register(new JSPController(API_URL));

	}

	public class DependencyBinder extends AbstractBinder {

		WundergroundClient client;
		WeatherServiceImpl service;
		WeatherServiceCache cacheService;
		SupportedCitiesProvider suppCitiesProvider;

		public void instantiateFields() throws IOException {
			client = new WundergroundClient(API_URL);
			suppCitiesProvider = new SupportedCitiesProvider(CITIES_JSON_PATH);
			service = new WeatherServiceImpl(client, suppCitiesProvider);
			cacheService = new WeatherServiceCache(client, suppCitiesProvider, new DbCache());
		}

		@Override
		protected void configure() {
			try {
				instantiateFields();
				//bind(cacheService).to(WeatherService.class);
				bind(service).to(WeatherService.class);
				bind(suppCitiesProvider).to(SupportedCitiesProvider.class);
			} catch (IOException ex) {
				log.error("SupportedCitiesProvider failed. Check if path (" + CITIES_JSON_PATH + ") is correct and if JSON file exists.", ex);
				Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

}
