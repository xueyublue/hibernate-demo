package sg.com.crimsonlogic.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sg.com.crimsonlogic.hibernate.entity.Course;
import sg.com.crimsonlogic.hibernate.entity.Instructor;
import sg.com.crimsonlogic.hibernate.entity.Review;

// Instructor 1 <> 9 	Course
// Course 	  1  > 9 	Review
public class HibernateOneToManyMapping {
	
	// Main
    public static void main( String[] args) {
    	
    	SessionFactory factory = Util.getSessionFactory();
		
    	try {
			
    		create(factory.getCurrentSession());
    		
			read(factory.getCurrentSession());
			
			update(factory.getCurrentSession());
			
			delete(factory.getCurrentSession());
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
		
		// create instructor and course at the same time
    	Instructor instructor = new Instructor("Joe", "Dane", "joe.dane@gmail.com");
		Course course = new Course("Beginning Java");
		course.setInstructor(instructor);
		session.save(instructor);
		session.save(course);
		
		// retrieve instructor first and then add course
		instructor = session.get(Instructor.class, 1);
		course = new Course("Beginning C#");
		course.setInstructor(instructor);	// option 1
//		instructor.addCourse(course);		// option 2 - calling helper method
		session.save(course);
		
		// add reviews to course
		Review review1 = new Review("Nice");
		Review review2 = new Review("Good");
		course.addReview(review1);
		course.addReview(review2);
		session.save(course);
		
		// commit
		session.getTransaction().commit();
		session.close();
	}
    
    public static void read(Session session) {	
    	
    	session.beginTransaction();
		
    	// get instructor
		Instructor instructor = session.get(Instructor.class, 1);
		
		// execute SQL if FetchType = LAZY
		for (Course course : instructor.getCourses()) {
			System.out.println(course);
		}
    	
		// commit
		session.getTransaction().commit();
		session.close();
	}
    
    public static void update(Session session) {	
    	
    	session.beginTransaction();
		
    	// update course's instructor_id
		Course course = session.get(Course.class, 1);
		Instructor instructor = session.get(Instructor.class, 1);
		if(instructor != null && course != null) {
			course.setInstructor(instructor);
		}
		
		session.save(course);
		
		// commit
		session.getTransaction().commit();
		session.close();
	}
    
    public static void delete(Session session) {	
    	
    	session.beginTransaction();
		
    	// delete course and reviews
    	// - it will actually delete all the reviews first based on course_id and then delete course
		Course course = session.get(Course.class, 2);
		if(course != null) {
			session.delete(course);
		}
		
		// commit
		session.getTransaction().commit();
		session.close();
	}
    
}
