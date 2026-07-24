package com.careplus.server.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/*
 * Creates a single sessions factory that will be used by other classes to access the MYSQL database
 */

public class HibernateUtil {

	/*
	 * A SessionFactory takes a while to build but is safe to share once it exists,
	 * so we make one and every ClientHandler thread uses it. Individual Sessions
	 * are NOT safe to share, which is why getSession() below hands out a new one
	 * every call instead of passing the same one around.
	 *
	 */
	public static SessionFactory sessionFactory = null;

	public HibernateUtil() {

		buildSessionFactory();
	}

	/*
	 * Only builds the factory if there isn't one already.
	 *  It runs once at server startup, before any client can connect.
	 */
	private static SessionFactory buildSessionFactory() {

		if (sessionFactory == null) {

			try {
				/*
				 * configure() with nothing passed in picks up hibernate.cfg.xml from the top
				 * of the classpath.
				 */
				sessionFactory = new Configuration().configure().buildSessionFactory();
				
				System.out.println("SessionFactory created successfully.");
			} catch (Exception e) {

				System.out.println("Initial SessionFactory creation failed: " + e.getMessage());
				e.printStackTrace();
			}
		}
		return sessionFactory;
	}

	/*
	 * Builds the factory again from scratch. We need this after
	 * DatabaseResetService drops and recreates the database.
	 */
	public static void reconnect() {
		try {
			if (sessionFactory != null && !sessionFactory.isClosed()) {
				sessionFactory.close();
			}
			/*
			 * Setting it to null is what lets buildSessionFactory() actually do anything,
			 * since it only builds when it finds nothing there.
			 */
			sessionFactory = null;
			buildSessionFactory();
			System.out.println("SessionFactory reconnected successfully.");
		} catch (Exception ex) {
			System.out.println("Failed to reconnect SessionFactory: " + ex.getMessage());
		}
	}

	/*
	 * Gives out a new Session each call instead of handing over the factory,
	 * because Sessions aren't thread safe and two client threads must never share
	 * one. Whoever calls this owns the session and has to close it, which
	 * BaseService.endSession does on every request.
	 *
	 * The check at the top rebuilds the factory if the database got reset since
	 * last time, so the services never have to know a reset happened. The reset
	 * stops the server first anyway, so no client threads are running while we're
	 * swapping the factory out.
	 */
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
