package com.tieto.wro.java.a17.wunderground;

import com.tieto.wro.java.a17.wunderground.model.Response;

public class TestObjectProvider {

    public Response getResponse(String kind) {
        Response response = new Response();
        switch (kind) {
            case "currentObservation": {
                response.setCurrentObservation(new Response.CurrentObservation());
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
}
