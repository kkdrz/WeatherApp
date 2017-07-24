package com.tieto.wro.java.a17.weather;

import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.wunderground.model.Response;

public class WundergroundResponseTransformer {

    public CityWeather transform(Response response) {
        if (response == null || response.getCurrentObservation() == null) {
            return null;
        }
        Response.CurrentObservation currObs = response.getCurrentObservation();

        CityWeather cw = new CityWeather(
                currObs.getDisplayLocation().getFull(),
                Double.parseDouble(currObs.getTempC()),
                currObs.getRelativeHumidity(),
                currObs.getWindDir(),
                currObs.getWeather(),
                currObs.getWindString(),
                currObs.getObservationTime());
        return cw;
    }
}
