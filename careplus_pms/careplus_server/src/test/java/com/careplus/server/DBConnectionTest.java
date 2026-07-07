package com.careplus.server;



import org.hibernate.Session;
import com.careplus.common.model.Person;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DBConnectionTest {
	public static int test() {
		try {
		
			Configuration config = new Configuration();
			
			config.configure().addAnnotatedClass(Person.class);
			
			SessionFactory sf = config.buildSessionFactory();
			
			Session session = sf.openSession();

			 session.beginTransaction();
			 
			Long count = session.createQuery("SELECT COUNT(p) FROM Person p", Long.class).getSingleResult();
			//end the transaction
	        session.getTransaction().commit();
	        
			System.out.println("Connected. Person rows: " + count); // should print 9

			session.close();
			sf.close();
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

}