package com.tieto.wro.java.a17.weather.provider.client;

import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.wunderground.TestObjectProvider;
import com.tieto.wro.java.a17.wunderground.model.Response;
import static org.junit.Assert.*;
import org.junit.Test;

public class WundergroundResponseTransformerTest {

	private final TestObjectProvider provider;
	private final WundergroundResponseTransformer transformer;

	public WundergroundResponseTransformerTest() {
		provider = new TestObjectProvider();
		transformer = new WundergroundResponseTransformer();
	}

	@Test
	public void When_ResponseHasCurrentObservation_Expect_TransformToCityWeatherCorrect() {
		Response response = provider.getResponse("currentObservation");
		Response.CurrentObservation obs = response.getCurrentObservation();

		CityWeather cityWeather = transformer.transform(response);

		assertEquals("The same location", obs.getDisplayLocation().getFull(), cityWeather.getLocation());
		assertEquals("The same temperatureCelsius", obs.getTempC(), cityWeather.getTemperatureCelsius().toString());
		assertEquals("The same relativeHumidity", obs.getRelativeHumidity(), cityWeather.getRelativeHumidity());
		assertEquals("The same windDirection", obs.getWindDir(), cityWeather.getWindDirection());
		assertEquals("The same weather", obs.getWeather(), cityWeather.getWeather());
		assertEquals("The same windString", obs.getWindString(), cityWeather.getWindString());
		assertEquals("The same weatherDate", obs.getObservationTime(), cityWeather.getWeatherDate());
	}

	@Test
	public void When_ResponseHasError_Expect_TransformReturnsNull() {
		Response response = provider.getResponse("error");

		CityWeather cityWeather = transformer.transform(response);

		assertNull(cityWeather);
	}

	@Test
	public void When_ResponseHasResults_Expect_TransformReturnsNull() {
		Response response = provider.getResponse("results");

		CityWeather cityWeather = transformer.transform(response);

		assertNull(cityWeather);
	}
}
