package com.tieto.wro.java.a17.wunderground;

import com.tieto.wro.java.a17.wunderground.client.WundergroundClient;
import com.tieto.wro.java.a17.wunderground.client.WundergroundIO;
import com.tieto.wro.java.a17.wunderground.model.Response;
import com.tieto.wro.java.a17.wunderground.model.Response.Results;
import javax.ws.rs.client.ClientBuilder;

public class App 
{
    private final WundergroundIO io;
    private final WundergroundClient client;

    public App() {
        io = new WundergroundIO();
        client = new WundergroundClient(ClientBuilder.newClient());
    }
    
    public static void main( String[] args )
    {
        App app = new App();
        app.run(null);
    }
    
    public void run(String zmw) {
        Response response;
        if (zmw == null || zmw.equals("")) {
            response = makeRequest(io.getCountryFromConsole(), io.getCityFromConsole());
        } else {
            response = makeRequest(zmw);
        }
        handleResponse(response);
    }
    
    public Response makeRequest(String country, String city) {
        return client.getWeather(country, city);
    }
    
    public Response makeRequest(String zmw) {
        return client.getWeather(zmw);
    }
    
    public void handleResponse(Response response) {
        switch(response.getResponseType()) {
            case CURRENT_OOBSERVATION: io.printWeather(response.getCurrentObservation()); break;
            case RESULTS: handleManyCities(response.getResults()); break;
            case ERROR: io.printError(); break;
            case INCORRECT: io.printIncorrect(); break;
        }
    }

    private void handleManyCities(Response.Results results) {
        io.printResults(results);
        Results.Result result = io.getResultFromConsole(results);
        run(result.getZmw());
    }
}
