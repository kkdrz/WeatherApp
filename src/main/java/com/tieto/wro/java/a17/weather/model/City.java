package com.tieto.wro.java.a17.weather.model;

import lombok.Data;

@Data
public class City {

	private String name;
	private String country;
	private String id;

	public City(String name, String country, String id) {
		this.name = name;
		this.country = country;
		this.id = id;
	}
	
}
