package sg.com.crimsonlogic.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sg.com.crimsonlogic.hibernate.entity.Course;
import sg.com.crimsonlogic.hibernate.entity.Student;

// Course 	  9  > 9 	Student
public class HibernateManyToManyMapping {
	
	// Main
    public static void main( String[] args) {
    	
    	SessionFactory factory = Util.getSessionFactory();
		
    	try {
    		
			prepareData(factory.getCurrentSession());
			
    		createMaping(factory.getCurrentSession());
    		
    		removeMapping(factory.getCurrentSession());
    		
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
		
		// create instructor and course first
    	Course course = new Course("Beginning Java");
		session.save(course);
		
		Student student = new Student("Darren", "Xue", 32);
		session.save(student);
		
		// commit
		session.getTransaction().commit();
		session.close();
	}
    
    public static void createMaping(Session session) {	
    	
    	session.beginTransaction();
		
    	// add course to student
    	Student student = session.get(Student.class, 1);
    	Course course = session.get(Course.class, 1);
    	if(student != null && course != null) {
    		student.addCourse(course);
        	session.save(student);
    	}
    	
		// commit
		session.getTransaction().commit();
		session.close();
	}
    
    public static void removeMapping(Session session) {	
    	
    	session.beginTransaction();
		
    	// remove course from student
    	Student student = session.get(Student.class, 1);
    	Course course = session.get(Course.class, 1);
    	if(student != null && course != null) {
    		student.removeCourse(course);
        	session.save(student);
    	}
		
		// commit
		session.getTransaction().commit();
		session.close();
	}
    
}
