package com.careplus.server.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/*
 * Creates a single sessions factory that will be used by other classes to access the MYSQL database
 */

public class HibernateUtil {

	/*
	 * A SessionFactory is expensive to build but is thread safe once constructed,
	 * so we build one and share it across every ClientHandler thread. Individual
	 * Sessions are not thread safe, which is why getSession() below hands out a new
	 * one per call instead of sharing a single session.
	 *
	 * We left the field mutable so DatabaseResetService can close the factory and
	 * rebuild it around a schema drop, which is what allows the reset feature to
	 * work while the server is running.
	 */
	public static SessionFactory sessionFactory = null;

	public HibernateUtil() {

		buildSessionFactory();
	}

	/*
	 * Builds the factory only when there is not one already, so repeated calls are
	 * harmless. The factory is created once during server startup, before any
	 * client can connect.
	 */
	private static SessionFactory buildSessionFactory() {

		if (sessionFactory == null) {

			try {
				/*
				 * configure() with no argument reads hibernate.cfg.xml from the classpath root.
				 * DatabaseResetService reuses that same file to recover the raw JDBC
				 * credentials, so connection settings live in exactly one place.
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
	 * Rebuilds the factory from scratch. Used after DatabaseResetService drops and
	 * recreates the schema, since the old factory's pooled connections still point
	 * at a database that no longer exists.
	 */
	public static void reconnect() {
		try {
			if (sessionFactory != null && !sessionFactory.isClosed()) {
				sessionFactory.close();
			}
			/*
			 * Nulling the field is what lets buildSessionFactory() do its work, since it
			 * only builds when it finds nothing there. Without this line a reconnect on an
			 * already closed factory would leave it closed.
			 */
			sessionFactory = null;
			buildSessionFactory();
			System.out.println("SessionFactory reconnected successfully.");
		} catch (Exception ex) {
			System.out.println("Failed to reconnect SessionFactory: " + ex.getMessage());
		}
	}

	/*
	 * Hands out a new Session per call rather than exposing the factory itself,
	 * because Sessions are not thread safe and must not be shared between client
	 * threads. Whoever calls this owns the session and is responsible for closing
	 * it, which BaseService.endSession does on every request.
	 *
	 * The check at the top rebuilds the factory if the database was reset since the
	 * last call, so services do not need to know a reset ever happened. The reset
	 * flow stops the server first, so no client threads are running while the
	 * factory is being swapped.
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
