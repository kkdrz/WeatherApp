package com.tieto.wro.java.a17.weather.service;

import com.tieto.wro.java.a17.weather.WundergroundResponseTransformer;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.wunderground.WundergroundPathBuilder;
import com.tieto.wro.java.a17.wunderground.client.WundergroundClient;
import com.tieto.wro.java.a17.wunderground.model.Response;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.*;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WeatherServiceImplTest {

    @Mock
    WundergroundClient client;
    @Mock
    WundergroundResponseTransformer transformer;
    @Mock
    WundergroundPathBuilder pathBuilder;

    private WeatherServiceImpl service;
    private final String NOT_SUPP_CITY = "City";
    private final String SUPP_CITY = "Wroclaw";

    @Before
    public void setUp() {
        service = new WeatherServiceImpl(client, transformer, pathBuilder);
    }

    @Test
    public void When_SupportedCity_Expect_GetCityWeatherReturnsCorrectCW() {
        Response response = new Response();
        CityWeather cityWeather = new CityWeather();
        when(pathBuilder.buildPath((String) notNull(), (String) notNull())).thenReturn("path");
        when(client.getWeather("path")).thenReturn(response);
        when(transformer.transform(response)).thenReturn(cityWeather);

        CityWeather cwResult = service.getCityWeather(SUPP_CITY);

        assertNotNull(cwResult);
        assertEquals(cityWeather, cwResult);
    }

    @Test
    public void When_CityDoesntExist_Expect_GetCityWeatherReturnsNull() {
        when(pathBuilder.buildPath("", NOT_SUPP_CITY)).thenReturn("path");
        when(client.getWeather("path")).thenReturn(null);
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
        when(pathBuilder.buildPath((String) notNull(), (String) notNull())).thenReturn("path");
        when(client.getWeather("path")).thenReturn(response);
        when(transformer.transform(response)).thenReturn(cityWeather);

        List<CityWeather> cwResult = service.getCitiesWeathers();

        assertFalse(cwResult.contains(null));
        assertFalse(cwResult.isEmpty());
    }

}
