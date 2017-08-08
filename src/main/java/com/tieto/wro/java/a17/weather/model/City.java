package com.tieto.wro.java.a17.weather.model;

import lombok.Data;

@Data
public class City {

	private String name;
	private String id;

	public City() {
	}

	public City(String name, String id) {
		this.name = name;
		this.id = id;
	}

}
