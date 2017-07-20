package com.tieto.wro.java.a17.wunderground;

import com.tieto.wro.java.a17.wunderground.client.WundergroundClient;
import com.tieto.wro.java.a17.wunderground.model.Response;
import com.tieto.wro.java.a17.wunderground.model.Response.Results;
import java.util.StringJoiner;
import javax.ws.rs.client.ClientBuilder;

public class App {

    private static final String API_KEY = "b6bfc129d8a2c4ea";
    private static final String API_URL = "http://api.wunderground.com/api/" + API_KEY;
    private final WundergroundIO io;
    private final WundergroundClient client;
    private final WundergroundPathBuilder pathBuilder;

    public App(WundergroundIO io, WundergroundClient client, WundergroundPathBuilder pathBuilder) {
        this.io = io;
        this.client = client;
        this.pathBuilder = pathBuilder;
    }

    public static void main(String[] args) {
        WundergroundIO io = new WundergroundIO();
        WundergroundClient client = new WundergroundClient(ClientBuilder.newClient(), API_URL);
        WundergroundPathBuilder pathBuilder = new WundergroundPathBuilder();

        App app = new App(io, client, pathBuilder);
        app.run();
    }

    public void run() {
        Response response = makeRequest(io.getCountryFromConsole(), io.getCityFromConsole());
        handleResponse(response);
    }

    public void run(String zmw) {
        handleResponse(makeRequest(zmw));
    }

    public Response makeRequest(String country, String city) {
        String path = pathBuilder.buildPath(country, city);
        return client.getWeather(path);
    }

    public Response makeRequest(String zmw) {
        String path = pathBuilder.buildPath(zmw);
        return client.getWeather(path);
    }

    public void handleResponse(Response response) {
        switch (response.getResponseType()) {
            case CURRENT_OOBSERVATION:
                io.printWeather(response.getCurrentObservation());
                break;
            case RESULTS:
                handleManyCities(response.getResults());
                break;
            case ERROR:
                io.printError();
                break;
            case INCORRECT:
                io.printIncorrect();
                break;
        }
    }

    private void handleManyCities(Response.Results results) {
        io.printResults(results);
        Results.Result result = io.getResultFromConsole(results);
        run(result.getZmw());
    }

}
