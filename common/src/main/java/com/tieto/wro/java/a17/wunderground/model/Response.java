package com.tieto.wro.java.a17.wunderground.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import lombok.Data;
import lombok.extern.log4j.Log4j;

@Log4j
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"version", "termsofService", "features", "error", "results", "currentObservation"})
@XmlRootElement(name = "response")
public class Response {

	public Response() {
	}

	protected String version;
	@XmlElement(required = true)
	protected String termsofService;
	@XmlElement(required = true)
	protected Response.Features features;
	@XmlElement(required = false)
	protected Response.Error error;
	@XmlElement(required = false)
	protected Response.Results results;
	@XmlElement(name = "current_observation", required = false)
	protected Response.CurrentObservation currentObservation;

	public ResponseType getResponseType() {
		if (!isValid()) {
			log.info("Response type is: INCORRECT.");
			return ResponseType.INCORRECT;
		}
		if (getCurrentObservation() != null) {
			log.info("Response type is: CURRENT_OBSERVATION.");
			return ResponseType.CURRENT_OBSERVATION;
		}
		if (getError() != null) {
			log.info("Response type is: ERROR.");
			return ResponseType.ERROR;
		}
		if (getResults() != null) {
			log.info("Response type is: RESULTS.");
			return ResponseType.RESULTS;
		}
		return ResponseType.INCORRECT;
	}

	private boolean isValid() {
		log.info("Checking if Response is valid.");
		return getCurrentObservation() != null
				^ getResults() != null
				^ getError() != null;
	}

	public enum ResponseType {
		ERROR, CURRENT_OBSERVATION, RESULTS, INCORRECT
	}

	@Data
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = {"type", "description"})
	public static class Error {

		@XmlElement(required = true)
		protected String type;
		@XmlElement(required = true)
		protected String description;

	}

	@Data
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = {"feature"})
	public static class Features {

		@XmlElement(required = true)
		protected String feature;

	}

	@Data
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = {"result"})
	public static class Results {

		protected List<Response.Results.Result> result;

		public List<Response.Results.Result> getResult() {
			if (result == null) {
				result = new ArrayList<Response.Results.Result>();
			}
			return this.result;
		}

		@Data
		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder
				= {"name", "city", "state", "country", "countryIso3166", "countryName", "zmw",
					"l"})
		public static class Result {

			@XmlElement(required = true)
			protected String name;
			@XmlElement(required = true)
			protected String city;
			@XmlElement(required = true)
			protected String state;
			@XmlElement(required = true)
			protected String country;
			@XmlElement(name = "country_iso3166", required = true)
			protected String countryIso3166;
			@XmlElement(name = "country_name", required = true)
			protected String countryName;
			@XmlElement(required = true)
			protected String zmw;
			@XmlElement(required = true)
			protected String l;

		}
	}

	@Data
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "current_observation", propOrder = {"image", "displayLocation", "observationLocation", "estimated", "stationId",
		"observationTime", "observationTimeRfc822", "observationEpoch", "localTimeRfc822", "localEpoch",
		"localTzString", "localTzLong", "localTzOffset", "weather", "temperatureString", "tempF", "tempC",
		"relativeHumidity", "windString", "windDir", "windDegrees", "windMph", "windGustMph", "windKph",
		"windGustKph", "pressureMb", "pressureIn", "pressureTrend", "dewpointString", "dewpointF", "dewpointC",
		"heatIndexString", "heatIndexF", "heatIndexC", "windchillString", "windchillF", "windchillC",
		"feelslikeString", "feelslikeF", "feelslikeC", "visibilityMi", "visibilityKm", "solarradiation", "uv",
		"precip1HrString", "precip1HrIn", "precip1HrMetric", "precipTodayString", "precipTodayIn",
		"precipTodayMetric", "icon", "iconUrl", "forecastUrl", "historyUrl", "obUrl"})
	public static class CurrentObservation {

		@XmlElement(required = true)
		protected Response.CurrentObservation.Image image;
		@XmlElement(name = "display_location", required = true)
		protected Response.CurrentObservation.DisplayLocation displayLocation;
		@XmlElement(name = "observation_location", required = true)
		protected Response.CurrentObservation.ObservationLocation observationLocation;
		@XmlElement(required = true)
		protected String estimated;
		@XmlElement(name = "station_id", required = true)
		protected String stationId;
		@XmlElement(name = "observation_time", required = true)
		protected String observationTime;
		@XmlElement(name = "observation_time_rfc822", required = true)
		protected String observationTimeRfc822;
		@XmlElement(name = "observation_epoch")
		protected String observationEpoch;
		@XmlElement(name = "local_time_rfc822", required = true)
		protected String localTimeRfc822;
		@XmlElement(name = "local_epoch")
		protected String localEpoch;
		@XmlElement(name = "local_tz_String", required = true)
		protected String localTzString;
		@XmlElement(name = "local_tz_long", required = true)
		protected String localTzLong;
		@XmlElement(name = "local_tz_offset")
		protected String localTzOffset;
		@XmlElement(required = true)
		protected String weather;
		@XmlElement(name = "temperature_string", required = true)
		protected String temperatureString;
		@XmlElement(name = "temp_f")
		protected String tempF;
		@XmlElement(name = "temp_c")
		protected String tempC;
		@XmlElement(name = "relative_humidity", required = true)
		protected String relativeHumidity;
		@XmlElement(name = "wind_string", required = true)
		protected String windString;
		@XmlElement(name = "wind_dir", required = true)
		protected String windDir;
		@XmlElement(name = "wind_degrees")
		protected String windDegrees;
		@XmlElement(name = "wind_mph")
		protected String windMph;
		@XmlElement(name = "wind_gust_mph")
		protected String windGustMph;
		@XmlElement(name = "wind_kph")
		protected String windKph;
		@XmlElement(name = "wind_gust_kph")
		protected String windGustKph;
		@XmlElement(name = "pressure_mb")
		protected String pressureMb;
		@XmlElement(name = "pressure_in")
		protected String pressureIn;
		@XmlElement(name = "pressure_trend")
		protected String pressureTrend;
		@XmlElement(name = "dewpoint_string", required = true)
		protected String dewpointString;
		@XmlElement(name = "dewpoint_f")
		protected String dewpointF;
		@XmlElement(name = "dewpoint_c")
		protected String dewpointC;
		@XmlElement(name = "heat_index_string", required = true)
		protected String heatIndexString;
		@XmlElement(name = "heat_index_f", required = true)
		protected String heatIndexF;
		@XmlElement(name = "heat_index_c", required = true)
		protected String heatIndexC;
		@XmlElement(name = "windchill_string", required = true)
		protected String windchillString;
		@XmlElement(name = "windchill_f", required = true)
		protected String windchillF;
		@XmlElement(name = "windchill_c", required = true)
		protected String windchillC;
		@XmlElement(name = "feelslike_string", required = true)
		protected String feelslikeString;
		@XmlElement(name = "feelslike_f")
		protected String feelslikeF;
		@XmlElement(name = "feelslike_c")
		protected String feelslikeC;
		@XmlElement(name = "visibility_mi", required = true)
		protected String visibilityMi;
		@XmlElement(name = "visibility_km", required = true)
		protected String visibilityKm;
		@XmlElement(required = true)
		protected String solarradiation;
		@XmlElement(name = "UV")
		protected String uv;
		@XmlElement(name = "precip_1hr_string", required = true)
		protected String precip1HrString;
		@XmlElement(name = "precip_1hr_in")
		protected String precip1HrIn;
		@XmlElement(name = "precip_1hr_metric")
		protected String precip1HrMetric;
		@XmlElement(name = "precip_today_string", required = true)
		protected String precipTodayString;
		@XmlElement(name = "precip_today_in")
		protected String precipTodayIn;
		@XmlElement(name = "precip_today_metric")
		protected String precipTodayMetric;
		@XmlElement(required = true)
		protected String icon;
		@XmlElement(name = "icon_url", required = true)
		@XmlSchemaType(name = "anyURI")
		protected String iconUrl;
		@XmlElement(name = "forecast_url", required = true)
		protected String forecastUrl;
		@XmlElement(name = "history_url", required = true)
		protected String historyUrl;
		@XmlElement(name = "ob_url", required = true)
		protected String obUrl;

		@Data
		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder
				= {"full", "city", "state", "stateName", "country", "countryIso3166", "zip",
					"magic", "wmo", "latitude", "longitude", "elevation"})
		public static class DisplayLocation {

			@XmlElement(required = true)
			protected String full;
			@XmlElement(required = true)
			protected String city;
			@XmlElement(required = true)
			protected String state;
			@XmlElement(name = "state_name", required = true)
			protected String stateName;
			@XmlElement(required = true)
			protected String country;
			@XmlElement(name = "country_iso3166", required = true)
			protected String countryIso3166;
			protected String zip;
			protected String magic;
			protected String wmo;
			protected String latitude;
			protected String longitude;
			protected String elevation;

		}

		@Data
		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder = {"url", "title", "link"})
		public static class Image {

			@XmlElement(required = true)
			@XmlSchemaType(name = "anyURI")
			protected String url;
			@XmlElement(required = true)
			protected String title;
			@XmlElement(required = true)
			@XmlSchemaType(name = "anyURI")
			protected String link;

		}

		@Data
		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder
				= {"full", "city", "state", "country", "countryIso3166", "latitude", "longitude",
					"elevation"})
		public static class ObservationLocation {

			@XmlElement(required = true)
			protected String full;
			@XmlElement(required = true)
			protected String city;
			@XmlElement(required = true)
			protected String state;
			@XmlElement(required = true)
			protected String country;
			@XmlElement(name = "country_iso3166", required = true)
			protected String countryIso3166;
			protected String latitude;
			protected String longitude;
			@XmlElement(required = true)
			protected String elevation;

		}

	}

}
