package com.careplus.server.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/*
 * Creates a single sessions factory that will be used by other classes to access the MYSQL database
 */

public class HibernateUtil {

	/*
	 * A SessionFactory is expensive to build and is itself thread safe once
	 * constructed, so one instance is shared by every ClientHandler thread. Sessions
	 * are not thread safe, which is why getSession() hands out a new one per call
	 * rather than reusing a shared session.
	 *
	 * The field is public and mutable so DatabaseResetService can tear the factory
	 * down and rebuild it around a schema drop. That flexibility is what makes the
	 * reset work, but it also means any caller could null this out mid request.
	 */
	public static SessionFactory sessionFactory = null;

	public HibernateUtil() {

		buildSessionFactory();
	}

	/*
	 * Not synchronized, so two threads arriving together can both see a null factory
	 * and each build one, leaving the loser's instance orphaned and unclosed. In
	 * practice the factory is built once during server startup before any client can
	 * connect, which is what keeps this from surfacing.
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
			 * Must be nulled explicitly, because buildSessionFactory() only does work when
			 * it finds a null field. Skipping this line would make reconnect a silent no op
			 * on an already closed factory.
			 */
			sessionFactory = null;
			buildSessionFactory();
			System.out.println("SessionFactory reconnected successfully.");
		} catch (Exception ex) {
			System.out.println("Failed to reconnect SessionFactory: " + ex.getMessage());
		}
	}

	/*
	 * Hands out a new Session per call rather than exposing the factory, because
	 * Sessions are not thread safe and must never be shared between client threads.
	 * Callers own what they receive and are responsible for closing it, which
	 * BaseService.endSession does.
	 *
	 * The null or closed check is a check then act race: two threads can both find a
	 * closed factory and both call reconnect(), or one can pass the check just as
	 * another closes the factory and then call openSession() on a dead instance.
	 * The reset flow stops the server before touching the factory, so no client
	 * threads are live at that moment, which is the only reason this is safe.
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
