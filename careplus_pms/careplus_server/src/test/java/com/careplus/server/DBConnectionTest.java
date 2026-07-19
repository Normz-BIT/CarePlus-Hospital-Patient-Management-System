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
			 * Deliberately builds its own throwaway SessionFactory instead of using
			 * HibernateUtil, so the check exercises the configuration from scratch and
			 * cannot pass just because the application already holds a working factory.
			 * The cost is that this duplicates HibernateUtil's bootstrap logic and will
			 * drift from it if that changes.
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
			 * Both closes sit on the success path only, so a failure above leaks the
			 * session and the factory along with their connections. Acceptable in a
			 * short lived test process, but it would matter if this ran repeatedly.
			 */
			session.close();
			sf.close();
			return 1;
		} catch (Exception e) {
			/*
			 * Any failure is collapsed into a 0 return, so the caller learns that the check
			 * failed but not why. The stack trace on stdout is the only diagnostic.
			 */
			e.printStackTrace();
		}

		return 0;
	}

}