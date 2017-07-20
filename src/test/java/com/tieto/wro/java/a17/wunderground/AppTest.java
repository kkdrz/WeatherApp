package com.tieto.wro.java.a17.wunderground;

import com.tieto.wro.java.a17.wunderground.client.WundergroundClient;
import com.tieto.wro.java.a17.wunderground.model.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AppTest {

    @Mock
    private WundergroundIO io;
    @Mock
    private WundergroundClient client;
    @Mock
    private WundergroundPathBuilder pathBuilder;
    private final TestObjectProvider provider;

    private final String country = "Poland";
    private final String city = "Wroclaw";
    private final String fullPath = "/conditions/q/" + country + "/" + city + ".xml";
    private final String cityPath = "/conditions/q/" + city + ".xml";
    private final String countryPath = "/conditions/q/" + country + ".xml";
    private App app;

    public AppTest() {
        provider = new TestObjectProvider();
    }

    @Before
    public void setUp() {
        app = new App(io, client, pathBuilder);
    }

    @Test
    public void When_CountryCityExist_Expect_IOPrintsCurrentObservation() {
        Response response = provider.getResponse("currentObservation");
        when(io.getCountryFromConsole()).thenReturn(country);
        when(io.getCityFromConsole()).thenReturn(city);
        when(pathBuilder.buildPath(country, city)).thenReturn(fullPath);
        when(client.getWeather(fullPath)).thenReturn(response);
        app.run();
        verify(io).printWeather(response.getCurrentObservation());
    }

}
