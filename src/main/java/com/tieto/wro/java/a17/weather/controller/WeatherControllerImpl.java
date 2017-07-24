package com.tieto.wro.java.a17.weather.controller;

import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.weather.service.WeatherServiceImpl;
import com.tieto.wro.java.a17.wunderground.App;
import com.tieto.wro.java.a17.wunderground.client.WundergroundClient;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.log4j.Log4j;

@Log4j
@Path("/weather")
public class WeatherControllerImpl {

    WeatherServiceImpl service;

    public WeatherControllerImpl() {
        WundergroundClient client = new WundergroundClient(App.API_URL);
        service = new WeatherServiceImpl(client);
        log.info("WundergroundController instantiated.");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCitiesWeathers() {
        log.info("getCitiesWeathers request invoked.");
        List<CityWeather> entities = service.getCitiesWeathers();

        if (entities == null || entities.isEmpty()) {
            log.warn("None CityWeather was found.");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No results could be found.")
                    .build();
        }

        log.info("Mapping entities to JSON.");
        return Response.status(Response.Status.OK)
                .entity(entities)
                .build();
    }

    @GET
    @Path("/{city}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCityWeather(@PathParam("city") String city) {
        log.info("getCityWeather request for city: \"" + city + "\" invoked.");
        CityWeather entity = service.getCityWeather(city);
        if (entity == null) {
            log.warn("CityWeather for city: \"" + city + "\" couldn't be found - null.");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No results could be found.")
                    .build();
        }

        log.info("Mapping CityWeather entity to JSON.");
        return Response.status(Response.Status.OK)
                .entity(entity)
                .build();
    }

}
