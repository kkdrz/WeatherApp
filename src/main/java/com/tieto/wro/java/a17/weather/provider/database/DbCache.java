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

@Log4j
public class DbCache implements CityWeatherProvider {

	private SessionFactory factory;

	public DbCache() {
		try {
			factory = new Configuration()
					.configure()
					.buildSessionFactory();
		} catch (HibernateException ex) {
			log.error("Initial SessionFactory creation failed." + ex);
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

	CityWeather query(String location) {
		try (Session session = factory.openSession()) {
			log.info("Quering cityWeather for location: " + location + " from DB.");

			session.beginTransaction();
			List<CityWeather> cw = session.createQuery("FROM CityWeather cw where lower(cw.location) like :city")
					.setParameter("city", "%" + location.toLowerCase() + "%")
					.list();
			log.info("Quering done.");

			if (cw.isEmpty()) {
				log.error("Queried city: \"" + location + "\" doesnt exist in database.");
				return null;
			}
			return cw.get(0);
		}
	}
}
