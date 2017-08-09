package com.tieto.wro.java.a17.weather.provider.database;

import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.weather.provider.CityWeatherProvider;
import java.util.List;
import lombok.extern.log4j.Log4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

@Log4j
public class DbCache implements CityWeatherProvider {

	private SessionFactory factory;

	public DbCache() {
		try {
			factory = new Configuration()
					.configure("hibernate.cfg.xml")
					.buildSessionFactory();
		} catch (HibernateException ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	@Override
	public CityWeather getCityWeather(City city) {
		return query(city.getName());
	}

	public void saveOrUpdate(CityWeather cityWeather) {
		try (Session session = factory.openSession()) {
			log.info("Saving/Updating cityWeather: " + cityWeather.getLocation() + " to DB.");
			session.beginTransaction();
			session.saveOrUpdate(cityWeather);
			session.getTransaction().commit();
			log.info("Saving/Updating done.");
		}
	}

	CityWeather query(String city) {
		try (Session session = factory.openSession()) {
			log.info("Quering cityWeather for location: " + city + " from DB.");
			session.beginTransaction();
			Query query = session.createQuery("from CityWeather cw where LCASE(cw.location) like :city");
			query.setParameter("city", "%" + city.toLowerCase() + "%");

			log.info("Quering done.");
			List<CityWeather> cw = query.list();
			return cw.get(0);
		}
	}
}
