package com.tieto.wro.java.a17.nioapp;

import io.vertx.core.DeploymentOptions;
import io.vertx.rxjava.core.AbstractVerticle;
import lombok.extern.log4j.Log4j;

@Log4j
public class MainVerticle extends AbstractVerticle {

	@Override
	public void start() throws Exception {

		vertx.rxDeployVerticle("com.tieto.wro.java.a17.weather.controller.VertxController",
				new DeploymentOptions().setConfig(config()).setInstances(4)).subscribe();

		vertx.rxDeployVerticle("com.tieto.wro.java.a17.weather.service.VertxWeatherService",
				new DeploymentOptions().setInstances(4)).subscribe();

		vertx.rxDeployVerticle("com.tieto.wro.java.a17.weather.provider.client.VertxWundergroundClient",
				new DeploymentOptions().setInstances(4)).subscribe();
	}

}
