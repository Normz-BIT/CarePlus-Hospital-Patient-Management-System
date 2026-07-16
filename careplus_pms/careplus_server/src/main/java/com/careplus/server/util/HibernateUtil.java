package com.careplus.server.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/*
 * Creates a single sessions factory that will be used by other classes to access the MYSQL database
 */

public class HibernateUtil {

	// only one is ever needed so make static and final
	// naturally multithreaded
	public static SessionFactory sessionFactory = null;

	public HibernateUtil() {

		buildSessionFactory();
	}

	private static SessionFactory buildSessionFactory() {

		if (sessionFactory == null) {

			try {
				sessionFactory = new Configuration().configure().buildSessionFactory();
				
				

				System.out.println("SessionFactory created successfully.");
			} catch (Exception e) {

				System.out.println("Initial SessionFactory creation failed: " + e.getMessage());
				e.printStackTrace();
			}
		}
		return sessionFactory;
	}

	public static void reconnect() {
		try {
			if (sessionFactory != null && !sessionFactory.isClosed()) {
				sessionFactory.close();
			}
			sessionFactory = null;
			buildSessionFactory();
			System.out.println("SessionFactory reconnected successfully.");
		} catch (Exception ex) {
			System.out.println("Failed to reconnect SessionFactory: " + ex.getMessage());
		}
	}

	// we only get the current session not the factory
	public static Session getSession() {

		if (sessionFactory == null || sessionFactory.isClosed()) {
			reconnect();
		}
		return sessionFactory.openSession();
	}

	public static void closeFactory() {

		if (sessionFactory != null && !sessionFactory.isClosed()) {

			sessionFactory.close();
			System.out.println("Session Factory Closed");
			return;
		}
		System.out.println("Unable to Close Session Factory: ");

	}

}
