package com.tieto.wro.java.a17.weather.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of="zmw")
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
