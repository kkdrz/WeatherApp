package com.tieto.wro.java.a17.weather;

import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.wunderground.model.Response;
import lombok.extern.log4j.Log4j;

@Log4j
public class WundergroundResponseTransformer {

    public CityWeather transform(Response response) {
        log.info("Transforming Response to CityWeather");
        if (response == null || response.getCurrentObservation() == null) {
            log.warn("Response is null or doesn't provide required informations. It can't be transformed.");
            return null;
        }
        Response.CurrentObservation currObs = response.getCurrentObservation();

        CityWeather cw = new CityWeather();
        cw.setLocation(currObs.getDisplayLocation().getFull());
        cw.setRelativeHumidity(currObs.getRelativeHumidity());
        cw.setTemperatureCelsius(Double.parseDouble(currObs.getTempC()));
        cw.setWeather(currObs.getWeather());
        cw.setWeatherDate(currObs.getObservationTime());
        cw.setWindDirection(currObs.getWindDir());
        cw.setWindString(currObs.getWindString());

        log.info("Transforming Response to CityWeather completed.");
        return cw;
    }
}
