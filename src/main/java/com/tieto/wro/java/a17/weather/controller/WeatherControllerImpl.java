package com.tieto.wro.java.a17.weather.controller;

import com.tieto.wro.java.a17.weather.WundergroundResponseTransformer;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.weather.service.WeatherService;
import com.tieto.wro.java.a17.weather.service.WeatherServiceImpl;
import com.tieto.wro.java.a17.wunderground.App;
import com.tieto.wro.java.a17.wunderground.WundergroundPathBuilder;
import com.tieto.wro.java.a17.wunderground.client.WundergroundClient;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/weather/")
public class WeatherControllerImpl implements WeatherController {
    
    WeatherService service;
    
    public WeatherControllerImpl() {
        WundergroundClient client = new WundergroundClient(ClientBuilder.newClient(), App.API_URL);
        WundergroundResponseTransformer transformer = new WundergroundResponseTransformer();
        WundergroundPathBuilder pathBuilder = new WundergroundPathBuilder();
        
        service = new WeatherServiceImpl(client, transformer, pathBuilder);
    }
    
    @Override
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCitiesWeathers() {
        List<CityWeather> entities = service.getCitiesWeathers();
        
        if (entities == null || entities.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No results could be found.")
                    .build();
        }
        
        return Response.status(Response.Status.OK)
                .entity(entities)
                .build();
    }
    
    @Override
    @GET
    @Path("{city}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCityWeather(@PathParam("city") String city) {
        CityWeather entity = service.getCityWeather(city);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No results could be found.")
                    .build();
        }
        
        return Response.status(Response.Status.OK)
                .entity(entity)
                .build();
    }
    
}
