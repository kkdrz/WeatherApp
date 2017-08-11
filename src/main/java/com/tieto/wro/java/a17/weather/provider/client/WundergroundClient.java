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

	public WundergroundClient(String API_URL) {
		this(API_URL, ClientBuilder.newClient(), new WundergroundResponseTransformer());
	}

	public WundergroundClient(String apiUrl, Client client, WundergroundResponseTransformer transformer) {
		this.client = client;
		this.transformer = transformer;
		this.API_URL = apiUrl;
		log.info("WundergroundClient with apiURL=" + API_URL + " is instantiated.");
	}

	@Override
	public CityWeather getCityWeather(City city) throws NotFoundException {
		Response response = getWeatherByZmw(city.getZmw());
		CityWeather cityWeather = transformer.transform(response);
		return cityWeather;
	}

	public Response getWeatherByZmw(String zmw) throws NotFoundException {
		String uri = buildUrl(zmw);
		return getResponseFromUrl(uri);
	}

	private Response getResponseFromUrl(String url) throws NotFoundException {
		log.info("Getting response from URL: " + url);
		return client.target(url)
					.request(MediaType.APPLICATION_XML)
					.get(Response.class);
	}

	protected String buildUrl(String zmw) {
		return UriBuilder.fromPath(API_URL)
				.path("/conditions/q/zmw:{zmw}.xml")
				.build(zmw)
				.toString();
	}

}
