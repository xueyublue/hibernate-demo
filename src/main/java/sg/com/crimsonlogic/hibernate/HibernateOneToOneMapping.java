package sg.com.crimsonlogic.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sg.com.crimsonlogic.hibernate.entity.Instructor;
import sg.com.crimsonlogic.hibernate.entity.InstructorDetail;

// Instructor 1 > 1 InstructorDetail
public class HibernateOneToOneMapping {
	
	// Main
    public static void main( String[] args) {
    	
    	SessionFactory factory = Util.getSessionFactory();
		
    	try {
			
//    		create(factory.getCurrentSession());
    		
//			update(factory.getCurrentSession());
			
//			delete(factory.getCurrentSession());
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 
		finally {
			factory.close();
		}
    }
    
    public static void create(Session session) {	
    	
    	session.beginTransaction();
		
		// create instructor and detail
    	InstructorDetail instructorDetail = new InstructorDetail("https://github.com/joedane", "Basketball");
		Instructor instructor = new Instructor("Joe", "Dane", "joe.dane@gmail.com");
		instructor.setInstructorDetail(instructorDetail);
		session.save(instructor);
		
		// create instructor only
		instructor = new Instructor("Joe", "Dane", "joe.dane@gmail.com");
		session.save(instructor);
		
		// create detail only
		instructorDetail = new InstructorDetail("https://github.com/joedane", "Basketball");
		session.save(instructorDetail);
		
		// commit
		session.getTransaction().commit();
		session.close();
	}
    
    public static void update(Session session) {	
    	
    	session.beginTransaction();
		
		// map detail to instructor
    	// - if instructor's detail is the same from the following queries, then following queries will not be executed
		Instructor instructor = session.get(Instructor.class, 1);
		InstructorDetail instructorDetail = session.get(InstructorDetail.class, 1);
		if(instructor != null && instructorDetail != null) {
			instructor.setInstructorDetail(instructorDetail);
			session.save(instructor);
		}
		
		// un-map detail to instructor
    	instructor = session.get(Instructor.class, 2);
		if(instructor != null) {
			instructor.setInstructorDetail(null);
			session.save(instructor);
		}
		
		// commit
		session.getTransaction().commit();
		session.close();
	}
    
    public static void delete(Session session) {	
    	
    	session.beginTransaction();
		
		// delete instructor and detail
    	// - detail will be deleted if instructor's detail is not null
    	Instructor instructor = session.get(Instructor.class, 3);
		if(instructor != null) {
			session.delete(instructor);
		}
		
		// delete detail only
		// - detail record cannot be deleted if there is instructor mapped to it
    	InstructorDetail instructorDetail = session.get(InstructorDetail.class, 4);
		if(instructorDetail != null) {
			session.delete(instructorDetail);
		}
		
		// commit
		session.getTransaction().commit();
		session.close();
	}
    
}
