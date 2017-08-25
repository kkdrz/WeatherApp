package com.tieto.wro.java.a17.weather.model;

import lombok.Data;

@Data
public class City {

	private String name;
	private String zmw;

	public City() {
	}

	public City(String name, String zmw) {
		this.name = name;
		this.zmw = zmw;
	}

}
