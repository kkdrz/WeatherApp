package com.tieto.wro.java.a17.wunderground;

import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.wunderground.model.Response;

public class TestObjectProvider {

	public Response getResponse(String kind) {
		Response response = new Response();
		switch (kind) {
			case "currentObservation": {
				response.setCurrentObservation(buildCurrentObservation());
				break;
			}
			case "results": {
				response.setResults(new Response.Results());
				break;
			}
			case "error": {
				response.setError(new Response.Error());
				break;
			}
			case "incorrect": {
				response.setCurrentObservation(new Response.CurrentObservation());
				response.setResults(new Response.Results());
				break;
			}
		}
		return response;
	}

	public CityWeather buildCityWeather(String city) {
		CityWeather cw = new CityWeather();
		cw.setLocation(city + ", hehe");
		cw.setRelativeHumidity("Humidity");
		cw.setTemperatureCelsius(12.24);
		cw.setWeather("Weder");
		cw.setWeatherDate("Data wedera");
		cw.setWindString("Wiatry");
		cw.setWindDirection("direkcje wiatrów");
		return cw;
	}

	private Response.CurrentObservation buildCurrentObservation() {
		Response.CurrentObservation.DisplayLocation dispLoc = new Response.CurrentObservation.DisplayLocation();
		dispLoc.setFull("Wroclaw, Poland");

		Response.CurrentObservation currObs = new Response.CurrentObservation();
		currObs.setDisplayLocation(dispLoc);
		currObs.setTempC("25.3");
		currObs.setRelativeHumidity("60%");
		currObs.setWindDir("NE");
		currObs.setWeather("Partly Cloudy");
		currObs.setWindString("Calm");
		currObs.setObservationTime("Last Updated on July 20, 2:48 PM CEST");
		return currObs;
	}
}
