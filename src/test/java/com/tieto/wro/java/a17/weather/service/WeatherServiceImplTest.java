package com.tieto.wro.java.a17.weather.service;

import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.weather.provider.CityWeatherProvider;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class WeatherServiceImplTest {

	@Mock
	CityWeatherProvider provider;

	private WeatherServiceImpl service;
	private final String NOT_SUPP_CITY_NAME = "City";

	private final List<City> SUPP_CITIES = getSupportedCities();
	private final City SUPP_CITY = SUPP_CITIES.get(0);
	private final String SUPP_CITY_NAME = SUPP_CITY.getName();

	@Before
	public void setUp() {
		service = new WeatherServiceImpl(SUPP_CITIES, provider);
	}

	@Test
	public void When_CityLowerUpperCase_Expect_GetCityWeatherReturnsCorrectCW() {
		CityWeather cityWeather = new CityWeather();
		when(provider.getCityWeather(Matchers.any())).thenReturn(cityWeather);

		CityWeather cwResultUpper = service.getCityWeather(SUPP_CITY_NAME.toUpperCase());
		CityWeather cwResultLower = service.getCityWeather(SUPP_CITY_NAME.toLowerCase());

		assertNotNull(cwResultUpper);
		assertNotNull(cwResultLower);
		assertEquals(cityWeather, cwResultUpper);
		assertEquals(cityWeather, cwResultLower);
	}

	@Test
	public void When_SupportedCity_Expect_GetCityWeatherReturnsCorrectCW() {
		CityWeather cityWeather = new CityWeather();
		when(provider.getCityWeather(SUPP_CITY)).thenReturn(cityWeather);

		CityWeather cwResult = service.getCityWeather(SUPP_CITY_NAME);

		assertNotNull(cwResult);
		assertEquals(cityWeather, cwResult);
	}

	@Test
	public void When_CityDoesntExist_Expect_GetCityWeatherReturnsNull() {
		CityWeather cwResult = service.getCityWeather(NOT_SUPP_CITY_NAME);

		assertNull(cwResult);
	}

	@Test
	public void When_GetCitiesWeathers_Expect_GetCitiesWeathersReturnsListWithNonNull() {
		CityWeather cityWeather = new CityWeather();
		when(provider.getCityWeather(Matchers.any(City.class))).thenReturn(cityWeather);

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
