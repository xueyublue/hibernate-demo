package sg.com.crimsonlogic.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import sg.com.crimsonlogic.hibernate.entity.Student;

public class HibernateCaching {
	
	// Main
    public static void main( String[] args) {
    	
    	SessionFactory factory = Util.getSessionFactory();
		
    	try {
			
    		prepareData(factory.getCurrentSession());
			
//    		basicCaching1(factory.getCurrentSession());
			
//    		basicCaching2(factory);
    		
    		hqlCaching2(factory);
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 
		finally {
			factory.close();
		}
    }
    
    public static void prepareData(Session session) {	
    	
    	session.beginTransaction();
		
		// create
		Student student1 = new Student("Joe", "Dane", Util.randomStudentAge());
		Student student2 = new Student("Danny", "Huang", Util.randomStudentAge());
		session.save(student1);
		session.save(student2);
		
		// commit
		session.getTransaction().commit();
		session.close();
	}
    
    public static void basicCaching1(Session session) {	
    	
    	System.out.println("***1st level caching testing***");System.out.println();
    	
    	session.beginTransaction();
		
    	// Caching - same SQL will be not be executed within same session
    	Student student = session.get(Student.class, 1);
    	System.out.println("id 1: " + student);System.out.println();
    	student = session.get(Student.class, 1);
    	System.out.println("SQL not executed:");
    	System.out.println("id 1: " + student);System.out.println();
    	student = session.get(Student.class, 2);
    	System.out.println("SQL executed due to different id detected:");
    	System.out.println("id 2: " + student);System.out.println();
    	
		// commit
		session.getTransaction().commit();
		session.close();
	}
    
    public static void basicCaching2(SessionFactory factory) {	
    	
    	System.out.println("***2nd level caching testing***");System.out.println();
    	
    	Session session1 = factory.getCurrentSession();
    	session1.beginTransaction();
		Student student = session1.get(Student.class, 1);
    	System.out.println("id 1: " + student);System.out.println();
    	session1.getTransaction().commit();
    	session1.close();
    	
    	Session session2 = factory.getCurrentSession();
    	session2.beginTransaction();
		student = session2.get(Student.class, 1);
    	System.out.println("SQL not executed if 2nd level caching enabled:");		// Student needs to be annotated with @Cacheable and @Cache
		System.out.println("id 1: " + student);System.out.println();
    	session2.getTransaction().commit();
    	session2.close();
	}
    
    public static void hqlCaching2(SessionFactory factory) {	
    	
    	System.out.println("***HQL 2nd level caching testing***");System.out.println();
    	
    	Session session1 = factory.getCurrentSession();
    	session1.beginTransaction();
		Query<Student> query = session1.createQuery("from Student where id=1");
		query.setCacheable(true);
		Student student = query.getSingleResult();
    	System.out.println("id 1: " + student);System.out.println();
    	session1.getTransaction().commit();
    	session1.close();
    	
    	Session session2 = factory.getCurrentSession();
    	session2.beginTransaction();
    	query = session2.createQuery("from Student where id=1");
		query.setCacheable(true);
		student = query.getSingleResult();
    	System.out.println("SQL not executed if HQL 2nd level caching enabled:");		// Student needs to be annotated with @Cacheable and @Cache
		System.out.println("id 1: " + student);System.out.println();
    	session2.getTransaction().commit();
    	session2.close();
	}
    
}
