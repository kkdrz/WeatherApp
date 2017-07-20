package com.tieto.wro.java.a17.wunderground.client;

import com.tieto.wro.java.a17.wunderground.model.Response;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;

public class WundergroundClient {

    private final String API_URL;
    private final Client client;

    public WundergroundClient(Client client, String apiUrl) {
        this.client = client;
        this.API_URL = apiUrl;
    }

    public Response getWeather(String path) {
        return getResponse(API_URL + path);
    }

    private Response getResponse(String URL) {
        return client.target(URL)
                .request(MediaType.APPLICATION_XML)
                .get(Response.class);
    }
    
}
