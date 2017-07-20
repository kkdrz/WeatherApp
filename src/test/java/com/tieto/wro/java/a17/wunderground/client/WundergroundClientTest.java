package com.tieto.wro.java.a17.wunderground.client;

import com.tieto.wro.java.a17.wunderground.model.Response;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.ws.rs.client.ClientBuilder;
import static net.jadler.Jadler.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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

    @Test
    public void When_ValidPathPolandWroclaw_Expect_CorrectResponseMappingCurrentObservation() throws FileNotFoundException {
        onRequest()
                .havingMethodEqualTo("GET")
                .havingPathEqualTo(PATH)
                .havingHeaderEqualTo("Accept", "application/xml")
                .respond()
                .withContentType("application/xml")
                .withBody(new FileReader(new File("src/main/resources/query_poland_wroclaw.xml").getAbsolutePath()));

        WundergroundClient client = new WundergroundClient(ClientBuilder.newClient(), "http://localhost:" + port());
        Response response = client.getWeather(PATH);

        assertNotNull(response.getCurrentObservation());
        assertNull(response.getResults());
        assertNull(response.getError());

    }

    @Test
    public void When_ValidPathPolandPila_Expect_CorrectResponseMappingResults() throws FileNotFoundException {
        onRequest()
                .havingMethodEqualTo("GET")
                .havingPathEqualTo(PATH)
                .havingHeaderEqualTo("Accept", "application/xml")
                .respond()
                .withContentType("application/xml")
                .withBody(new FileReader(new File("src/main/resources/query_pila.xml").getAbsolutePath()));

        WundergroundClient client = new WundergroundClient(ClientBuilder.newClient(), "http://localhost:" + port());
        Response response = client.getWeather(PATH);

        assertNotNull(response.getResults());
        assertNull(response.getCurrentObservation());
        assertNull(response.getError());
    }

    @Test
    public void When_ValidPathRandomLetters_Expect_CorrectResponseMappingError() throws FileNotFoundException {
        onRequest()
                .havingMethodEqualTo("GET")
                .havingPathEqualTo(PATH)
                .havingHeaderEqualTo("Accept", "application/xml")
                .respond()
                .withContentType("application/xml")
                .withBody(new FileReader(new File("src/main/resources/query_not_found.xml").getAbsolutePath()));

        WundergroundClient client = new WundergroundClient(ClientBuilder.newClient(), "http://localhost:" + port());
        Response response = client.getWeather(PATH);

        assertNotNull(response.getError());
        assertNull(response.getCurrentObservation());
        assertNull(response.getResults());
    }

}
