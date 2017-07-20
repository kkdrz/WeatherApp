package com.tieto.wro.java.a17.wunderground.client;

import com.tieto.wro.java.a17.wunderground.model.Response;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.ws.rs.client.ClientBuilder;
import static net.jadler.Jadler.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WundergroundClientTest {

    private final String PATH = "/conditions/q/Poland/Wroclaw.xml";

    @Before
    public void setUp() {
        initJadler();
    }

    @After
    public void tearDown() {
        closeJadler();
    }
    
    private void jadlerRespondWith(String xmlName) throws FileNotFoundException {
        onRequest()
                .havingMethodEqualTo("GET")
                .respond()
                .withContentType("application/xml")
                .withBody(new FileReader(new File("src/main/resources/" + xmlName +".xml").getAbsolutePath()));
    }

    @Test
    public void When_ValidPathPolandWroclaw_Expect_CorrectResponseMappingCurrentObservation() throws FileNotFoundException {
        jadlerRespondWith("query_poland_wroclaw");

        WundergroundClient client = new WundergroundClient(ClientBuilder.newClient(), "http://localhost:" + port());
        Response response = client.getWeather(PATH);

        assertNotNull(response.getCurrentObservation());
        assertNull(response.getResults());
        assertNull(response.getError());

    }

    @Test
    public void When_ValidPathPolandPila_Expect_CorrectResponseMappingResults() throws FileNotFoundException {
        jadlerRespondWith("query_pila");

        WundergroundClient client = new WundergroundClient(ClientBuilder.newClient(), "http://localhost:" + port());
        Response response = client.getWeather(PATH);

        assertNotNull(response.getResults());
        assertNull(response.getCurrentObservation());
        assertNull(response.getError());
    }

    @Test
    public void When_ValidPathRandomLetters_Expect_CorrectResponseMappingError() throws FileNotFoundException {
        jadlerRespondWith("query_not_found");
        
        WundergroundClient client = new WundergroundClient(ClientBuilder.newClient(), "http://localhost:" + port());
        Response response = client.getWeather(PATH);

        assertNotNull(response.getError());
        assertNull(response.getCurrentObservation());
        assertNull(response.getResults());
    }

}
