package com.tieto.wro.java.a17.weather.service;

import com.tieto.wro.java.a17.weather.WundergroundResponseTransformer;
import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.wunderground.client.WundergroundClient;
import com.tieto.wro.java.a17.wunderground.model.Response;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WeatherServiceImplTest {

	@Mock
	WundergroundClient client;
	@Mock
	WundergroundResponseTransformer transformer;

	private WeatherServiceImpl service;
	private final String NOT_SUPP_CITY = "City";
	private final String SUPP_CITY = "Wroclaw";

	@Before
	public void setUp() {
		service = new WeatherServiceImpl(client, transformer, getSupportedCities());
	}

	@Test
	public void When_SupportedCity_Expect_GetCityWeatherReturnsCorrectCW() {
		Response response = new Response();
		CityWeather cityWeather = new CityWeather();
		when(client.getWeatherById(Matchers.anyString())).thenReturn(response);
		when(transformer.transform(response)).thenReturn(cityWeather);

		CityWeather cwResult = service.getCityWeather(SUPP_CITY);

		assertNotNull(cwResult);
		assertEquals(cityWeather, cwResult);
	}

	@Test
	public void When_CityDoesntExist_Expect_GetCityWeatherReturnsNull() {
		when(client.getWeather("Poland", SUPP_CITY.toLowerCase())).thenReturn(null);
		when(transformer.transform(null)).thenReturn(null);

		CityWeather cwResult = service.getCityWeather(NOT_SUPP_CITY);

		assertNull(cwResult);
	}

	@Test
	public void When_NotSupportedCity_Expect_GetCityWeatherReturnsNull() {
		CityWeather cwResult = service.getCityWeather(NOT_SUPP_CITY);
		assertNull(cwResult);
	}

	@Test
	public void When_GetCitiesWeathers_Expect_GetCitiesWeathersReturnsListWithNonNull() {
		Response response = new Response();
		CityWeather cityWeather = new CityWeather();
		when(client.getWeatherById(Matchers.anyString())).thenReturn(response);
		when(transformer.transform(response)).thenReturn(cityWeather);

		List<CityWeather> cwResult = service.getCitiesWeathers();

		assertFalse(cwResult.contains(null));
		assertFalse(cwResult.isEmpty());
	}

	private List<City> getSupportedCities() {
		List<City> cities = Arrays.asList(
				new City("wroclaw", "00000.7.12424"),
				new City("lodz", "00000.102.12465"),
				new City("czestochowa", "00000.484.12550"),
				new City("bielsko-biala", "00000.70.12600"),
				new City("ulkokalla", "00000.30.02907"));
		return cities;
	}

}
