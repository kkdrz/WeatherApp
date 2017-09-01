package com.tieto.wro.java.a17.wunderground.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import lombok.Data;
import lombok.extern.log4j.Log4j;

@Log4j
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "response")
public class Response {

	public Response() {
	}

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
	@XmlType(name = "")
	public static class Error {

		@XmlElement(required = true)
		protected String type;
		@XmlElement(required = true)
		protected String description;

	}

	@Data
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "")
	public static class Results {

		protected List<Response.Results.Result> result;

		public List<Response.Results.Result> getResult() {
			if (result == null) {
				result = new ArrayList<>();
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
	@XmlType(name = "current_observation")
	public static class CurrentObservation {

		@XmlElement(name = "wind_string", required = true)
		protected String windString;
		@XmlElement(name = "wind_dir", required = true)
		protected String windDir;
		@XmlElement(name = "observation_time", required = true)
		protected String observationTime;
		@XmlElement(name = "temp_c")
		protected String tempC;
		@XmlElement(required = true)
		protected String weather;
		@XmlElement(name = "relative_humidity", required = true)
		protected String relativeHumidity;
		@XmlElement(name = "display_location", required = true)
		protected Response.CurrentObservation.DisplayLocation displayLocation;

		@Data
		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder = {"full"})
		public static class DisplayLocation {

			@XmlElement(required = true)
			protected String full;

		}

	}

}
