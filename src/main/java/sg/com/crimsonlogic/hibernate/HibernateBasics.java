package sg.com.crimsonlogic.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import sg.com.crimsonlogic.hibernate.entity.Student;

public class HibernateBasics {
	
	// Main
    public static void main( String[] args) {
    	
    	SessionFactory factory = Util.getSessionFactory();
		
    	try {
			
    		create(factory.getCurrentSession());
			
			read(factory.getCurrentSession(), 1);
			
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
    	
    	System.out.println("... create ...");
    	
    	session.beginTransaction();
		
		// create
		Student student1 = new Student("Joe", "Dane", Util.randomStudentAge());
		session.save(student1);
		
		Student student2 = new Student();
		student2.setFirstName("Danny");
		student2.setLastName("Huang");
		student2.setAge(Util.randomStudentAge());
		session.save(student2);
		
		System.out.println();
		
		// commit
		session.getTransaction().commit();
		session.close();
	}
    
    public static void read(Session session, int id) {	
    	
    	System.out.println("... read ...");
    	
    	session.beginTransaction();
		
		// Basic - query by Primary Key
		Student student = session.get(Student.class, id);
		if(student != null) {
			System.out.println("Result: " + student.toString());
		}
		System.out.println();
		
		// HQL - query all records
		List<Student> students = session.createQuery("from Student", Student.class).getResultList();
		System.out.println("Result size: " + students.size());System.err.println();
		
		// HQL - query selected columns
		List<Object[]> resultList = session.createQuery("select id, firstName from Student").getResultList();
		System.out.println("HQL selected column:");
		for (Object[] objects : resultList) {
			System.out.println(objects[0].toString() + ", " + objects[1].toString());
		}
		System.out.println();
		
		// HQL - sum age
		Long sumAge = (Long) session.createQuery("select sum(age) from Student").uniqueResult();
		System.out.println("Sum age: " + sumAge); System.out.println();
		
		// HQL - query with where conditions
		Query<Student> query = session.createQuery("from Student s where s.age between :age1 and :age2 order by s.firstName asc", Student.class);
		query.setParameter("age1", 12);
		query.setParameter("age2", 15);
		students = query.getResultList();
		System.out.println("Result size: " + students.size());System.err.println();
		
		// HQL - query with LIKE condition
		students = session.createQuery("from Student s where s.firstName like '%Danny'", Student.class).getResultList();
		System.out.println("Result size: " + students.size());System.err.println();
		
		// HQL - pagination
		query = session.createQuery("from Student", Student.class);
		query.setFirstResult(1); 	// skip first X records
		query.setMaxResults(1);		// get X record
		query.getResultList();
		System.out.println("HQL pagination: " + students.size());System.out.println();
		
		// Named Query - query single record
		query = session.getNamedQuery("student.id");
		query.setParameter("id", 1);
		student =  query.getSingleResult();
		System.out.println("Named Query: " + student);System.out.println();
		
		// Named Native Query - query single record
		query = session.getNamedNativeQuery("student.id.native");
		query.setParameter("id", 1);
		student =  query.getSingleResult();
		System.out.println("Named Native Query: " + student);System.out.println();
		
		// commit
		session.getTransaction().commit();
		session.close();
	}
    
    public static void update(Session session) {
    	
    	System.out.println("... udpate ...");
    	
    	session.beginTransaction();
		
    	// Basic Update - manipulate the object directly, there is no need to call session.save(object)
    	// 		the object will be persisted to database as long as session.getTransaction.commit() called.
    	Student student = session.get(Student.class, 1);
		if(student != null) {
			student.setFirstName("Joee Udpated");
		}
		
		// HQL Update
		Query<?> query = session.createQuery("update Student set age=:age where id=:id");
		query.setParameter("age", 14);
		query.setParameter("id", 1);
		// session will persist all the pending modifications
		//		in this case, there two SQLs will be executed
		query.executeUpdate();		
		
		session.getTransaction().commit();
		session.close();
	}
    
    public static void delete(Session session) {
    	
    	System.out.println("... delete ...");
    	
    	session.beginTransaction();
		
    	// Basic Delete - SQl will not be executed until session commit called
    	Student student = session.get(Student.class, 1);
		if(student != null) {
			session.delete(student);
		}
    	
		// HQL Delete
		Query<?> query = session.createQuery("delete from Student where id=:id");
		query.setParameter("id", 1);
		// session will persist all the pending modifications
		//		in this case, there two SQLs will be executed
		query.executeUpdate();
		
		session.getTransaction().commit();
		session.close();
	}
    
}
