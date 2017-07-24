package com.tieto.wro.java.a17.wunderground;

import com.tieto.wro.java.a17.wunderground.model.Response;

public class TestObjectProvider {

    public Response getResponse(String kind) {
        Response response = new Response();
        switch (kind) {
            case "currentObservation": {
                response.setCurrentObservation(buildCurrentObservation());
                break;
            }
            case "results": {
                response.setResults(new Response.Results());
                break;
            }
            case "error": {
                response.setError(new Response.Error());
                break;
            }
            case "incorrect": {
                response.setCurrentObservation(new Response.CurrentObservation());
                response.setResults(new Response.Results());
                break;
            }
        }
        return response;
    }

    private Response.CurrentObservation buildCurrentObservation() {
        Response.CurrentObservation.DisplayLocation dispLoc = new Response.CurrentObservation.DisplayLocation();
        dispLoc.setFull("Wroclaw, Poland");

        Response.CurrentObservation currObs = new Response.CurrentObservation();
        currObs.setDisplayLocation(dispLoc);
        currObs.setTempC("25.3");
        currObs.setRelativeHumidity("60%");
        currObs.setWindDir("NE");
        currObs.setWeather("Partly Cloudy");
        currObs.setWindString("Calm");
        currObs.setObservationTime("Last Updated on July 20, 2:48 PM CEST");
        return currObs;
    }
}
