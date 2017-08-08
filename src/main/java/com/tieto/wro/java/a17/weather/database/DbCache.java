package com.tieto.wro.java.a17.weather.database;

import com.tieto.wro.java.a17.weather.model.CityWeather;
import lombok.extern.log4j.Log4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Log4j
public class DbCache {

	private SessionFactory factory;

	public DbCache() {
		try {
			factory = new Configuration()
					.configure("hibernate.cfg.xml")
					.buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public void insert(CityWeather cityWeather) {
		try (Session session = factory.openSession()) {
			log.info("Saving cityWeather for location: " + cityWeather.getLocation() + " to DB.");
			session.beginTransaction();
			session.save(cityWeather);
			session.getTransaction().commit();
			log.info("Saving done.");
		}
	}

	public void update(CityWeather cityWeather) {
		try (Session session = factory.openSession()) {
			log.info("Updating cityWeather: " + cityWeather.getLocation() + " to DB.");
			session.beginTransaction();
			session.saveOrUpdate(cityWeather);
			session.getTransaction().commit();
			log.info("Updating done.");
		}
	}

	public CityWeather query(String location) {
		try (Session session = factory.openSession()) {
			log.info("Quering cityWeather for location: " + location + " from DB.");
			session.beginTransaction();
			CityWeather cityWeather = session.get(CityWeather.class, location);
			session.getTransaction().commit();
			log.info("Quering done.");
			return cityWeather;
		}
	}

}
