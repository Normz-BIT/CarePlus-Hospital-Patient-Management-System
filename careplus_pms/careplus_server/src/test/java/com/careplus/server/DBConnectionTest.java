package com.careplus.server;



import org.hibernate.Session;
import com.careplus.common.model.Person;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/*
 * Connectivity smoke check for the Hibernate configuration.
 *
 * Not a JUnit test itself, despite the name: it is a plain helper that TestClass
 * drives. It answers one question, whether hibernate.cfg.xml points at a
 * reachable database with a readable person table, so a failure here means the
 * environment is wrong rather than the application logic.
 *
 * Returns a status code rather than throwing so the caller can assert on it.
 */
public class DBConnectionTest {
	public static int test() {
		try {

			/*
			 * Builds its own SessionFactory rather than going through HibernateUtil, so
			 * the check tests the configuration from scratch. Reusing the application's
			 * factory would let the test pass simply because a working one already
			 * existed, which would not tell us the config file is correct.
			 */
			Configuration config = new Configuration();

			config.configure().addAnnotatedClass(Person.class);

			SessionFactory sf = config.buildSessionFactory();

			Session session = sf.openSession();

			 session.beginTransaction();

			/*
			 * COUNT rather than a full select, so the check stays cheap and does not depend
			 * on any particular row existing. It touches Person, the joined inheritance
			 * root, which means a mapping error anywhere in that hierarchy surfaces here.
			 */
			Long count = session.createQuery("SELECT COUNT(p) FROM Person p", Long.class).getSingleResult();
			//end the transaction
	        session.getTransaction().commit();

			/*
			 * The expected count is tied to the sample data in
			 * careplus_create_database.sql, so this trailing note goes stale whenever that
			 * script changes. Nothing asserts on the number.
			 */
			System.out.println("Connected. Person rows: " + count); // should print 9

			/*
			 * TODO: close the session and factory in a finally block so they are released
			 * even when the query above throws.
			 */
			session.close();
			sf.close();
			return 1;
		} catch (Exception e) {
			/*
			 * The stack trace is printed because the return value only reports whether the
			 * check passed, and when it fails the reason is what we actually need to see.
			 */
			e.printStackTrace();
		}

		return 0;
	}

}