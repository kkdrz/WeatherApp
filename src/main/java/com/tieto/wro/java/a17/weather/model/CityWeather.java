package com.tieto.wro.java.a17.weather.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "CityWeather")
public class CityWeather implements Serializable {

	public CityWeather() {
	}

	@Id
	@Column(name = "location")
	private String location;
	@Column(name = "temperature_celsius")
	private Double temperatureCelsius;
	@Column(name = "relative_humidity")
	private String relativeHumidity;
	@Column(name = "wind_direction")
	private String windDirection;
	@Column(name = "weather")
	private String weather;
	@Column(name = "wind_string")
	private String windString;
	@Column(name = "weather_date")
	private String weatherDate;

}
