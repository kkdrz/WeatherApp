package com.tieto.wro.java.a17.weather.provider.client;

import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.weather.provider.CityWeatherProvider;
import com.tieto.wro.java.a17.wunderground.model.Response;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import lombok.extern.log4j.Log4j;

@Log4j
public class WundergroundClient implements CityWeatherProvider {

	private final Client client;
	private final WundergroundResponseTransformer transformer;
	private String API_URL;

	public WundergroundClient() {
		this(ClientBuilder.newClient(), new WundergroundResponseTransformer(), "http://localhost:8089/api.wunderground.com/api/b6bfc129d8a2c4ea");
	}

	public WundergroundClient(String API_URL) {
		this(ClientBuilder.newClient(), new WundergroundResponseTransformer(), API_URL);
	}

	public WundergroundClient(Client client, WundergroundResponseTransformer transformer, String apiUrl) {
		this.client = client;
		this.transformer = transformer;
		this.API_URL = apiUrl;
		log.info("WundergroundClient with apiURL=" + API_URL + " is instantiated.");
	}

	@Override
	public CityWeather getCityWeather(City city) {
		Response response = getWeatherByZmw(city.getZmw());
		CityWeather cityWeather = transformer.transform(response);
		return cityWeather;
	}

	public Response getWeatherByZmw(String zmw) {
		String uri = buildUri(zmw);
		return getResponse(uri);
	}

	private Response getResponse(String uri) {
		log.info("Getting response from URL: " + uri);
		Response response;
		try {
			response = client.target(uri)
					.request(MediaType.APPLICATION_XML)
					.get(Response.class);
		} catch (NotFoundException e) {
			log.error("Response from URL: " + uri + " not found.");
			return null;
		}
		log.info("Response received.");
		return response;
	}

	protected String buildUri(String zmw) {
		return UriBuilder.fromPath(API_URL)
				.path("/conditions/q/zmw:{zmw}.xml")
				.build(zmw)
				.toString();
	}

}
