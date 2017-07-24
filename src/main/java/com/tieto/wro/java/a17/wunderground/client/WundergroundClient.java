package com.tieto.wro.java.a17.wunderground.client;

import com.tieto.wro.java.a17.wunderground.model.Response;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import lombok.extern.log4j.Log4j;

@Log4j
public class WundergroundClient {

    private final String API_URL;
    private final Client client;

    public WundergroundClient(String API_URL) {
        this(ClientBuilder.newClient(), API_URL);
    }

    public WundergroundClient(Client client, String apiUrl) {
        this.API_URL = apiUrl;
        this.client = client;
        log.info("WundergroundClient with apiURL=" + apiUrl + " is instantiated.");
    }

    public Response getWeather(String path) {
        return getResponse(API_URL + path);
    }

    private Response getResponse(String URL) {
        log.info("Getting response from URL: " + URL);
        return client.target(URL)
                .request(MediaType.APPLICATION_XML)
                .get(Response.class);
    }

}
