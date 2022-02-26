package sg.com.crimsonlogic.hibernate;

import java.util.List;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import sg.com.crimsonlogic.hibernate.dto.StudentDTO;
import sg.com.crimsonlogic.hibernate.entity.Course;
import sg.com.crimsonlogic.hibernate.entity.Student;

public class HibernateCriteria {
	
	// Main
    public static void main( String[] args) {
    	
    	SessionFactory factory = Util.getSessionFactory();
		
    	try {
			
    		prepareData(factory.getCurrentSession());
			
			readNew(factory.getCurrentSession());
			
//    		readMultipleRoots(factory.getCurrentSession());
    		
//			readOld(factory.getCurrentSession());
			
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
		
		Course course1 = new Course("Beginning Java");
		Course course2 = new Course("Beginning C#");
		session.save(course1);
		session.save(course2);
		
		student1.addCourse(course1);
		session.save(student1);
		
		// commit
		session.getTransaction().commit();
		session.close();
	}
    
    public static void readNew(Session session) {	
    	
    	session.beginTransaction();
		
		// Criteria - query all records
    	CriteriaBuilder builer = session.getCriteriaBuilder();
    	CriteriaQuery<Student> criteriaQuery = builer.createQuery(Student.class);
    	Root<Student> root = criteriaQuery.from(Student.class);
    	criteriaQuery.select(root);
    	List<Student> students = session.createQuery(criteriaQuery).list();
    	students.forEach(System.out::println);System.out.println();
    	
		// Criteria - query by Primary Key - single result expected
    	builer = session.getCriteriaBuilder();
    	criteriaQuery = builer.createQuery(Student.class);
    	root = criteriaQuery.from(Student.class);
    	criteriaQuery.where(builer.equal(root.get("id"), 1));	// where id=1
    	criteriaQuery.select(root);
    	students = session.createQuery(criteriaQuery).list();
    	students.forEach(System.out::println);System.out.println();
    	
		// Criteria - query by Parameter - single result expected
    	builer = session.getCriteriaBuilder();
    	criteriaQuery = builer.createQuery(Student.class);
    	root = criteriaQuery.from(Student.class);
    	ParameterExpression<Integer> idParam = builer.parameter(Integer.class);
    	criteriaQuery.where(builer.equal(root.get("id"), idParam));				// where id=:id
    	criteriaQuery.select(root);
    	Query<Student> query = session.createQuery(criteriaQuery);
    	query.setParameter(idParam, 1);											// pass in parameter
    	query.list();
    	students.forEach(System.out::println);System.out.println();

    	// Criteria - aggregate functions - count
    	builer = session.getCriteriaBuilder();
    	CriteriaQuery<Long> criteriaQuery_count = builer.createQuery(Long.class);
    	root = criteriaQuery_count.from(Student.class);
    	criteriaQuery_count.select(builer.count(root));
    	Long count = session.createQuery(criteriaQuery_count).getSingleResult();
    	System.out.println("Count: " + count);;System.out.println();
    	
    	// Criteria - aggregate functions - max id
    	builer = session.getCriteriaBuilder();
    	CriteriaQuery<Integer> criteriaQuery_max = builer.createQuery(Integer.class);
    	root = criteriaQuery_max.from(Student.class);
    	criteriaQuery_max.select(builer.max(root.get("id")));
    	Integer maxId = session.createQuery(criteriaQuery_max).getSingleResult();
    	System.out.println("Max id: " + maxId);;System.out.println();
    	
    	// Criteria - query single column
    	builer = session.getCriteriaBuilder();
    	CriteriaQuery<Integer> criteriaQuery_OneColumn = builer.createQuery(Integer.class);
    	root = criteriaQuery_OneColumn.from(Student.class);
    	criteriaQuery_OneColumn.select(root.get("id"));
    	List<Integer> idList = session.createQuery(criteriaQuery_OneColumn).list();
    	idList.forEach(System.out::println);System.out.println();
    	
    	// Criteria - query multiple columns
    	builer = session.getCriteriaBuilder();
    	CriteriaQuery<Object[]> criteriaQuery_MulColumns = builer.createQuery(Object[].class);
    	root = criteriaQuery_MulColumns.from(Student.class);
    	Path<Object> idPath = root.get("id");
    	Path<Object> firstNamePath = root.get("firstName");
    	criteriaQuery_MulColumns.select(builer.array(idPath, firstNamePath));
//    	criteriaQuery_MulColumns.multiselect(idPath, firstNamePath);			// option 2
    	List<Object[]> columnList = session.createQuery(criteriaQuery_MulColumns).list();
    	for (Object[] objects : columnList) {
    		System.out.println("Multiple Columns [id=" + objects[0] + ", " + "firstName=" + objects[1] + "]");
		}
    	System.out.println();
    	
    	// Criteria - return DTO
    	builer = session.getCriteriaBuilder();
    	CriteriaQuery<StudentDTO> criteriaQuery_DTO = builer.createQuery(StudentDTO.class);
    	root = criteriaQuery_DTO.from(Student.class);
    	criteriaQuery_DTO.select(builer.construct(StudentDTO.class, idPath, firstNamePath));
    	List<StudentDTO> dtoList = session.createQuery(criteriaQuery_DTO).list();
    	dtoList.forEach(System.out::println);System.out.println();

    	// Criteria - Tuple
    	builer = session.getCriteriaBuilder();
    	CriteriaQuery<Tuple> criteriaQuery_tuple = builer.createQuery(Tuple.class);
    	root = criteriaQuery_tuple.from(Student.class);
    	criteriaQuery_tuple.multiselect(idPath, firstNamePath);
    	List<Tuple> tupleList = session.createQuery(criteriaQuery_tuple).list();
    	for (Tuple tuple : tupleList) {
    		System.out.println("Tuple [id=" + tuple.get(idPath) + ", " + "firstName=" + tuple.get(firstNamePath) + "]");
		}
    	System.out.println();
    	
    	// Criteria - where (1 =< id <= 2)
    	builer = session.getCriteriaBuilder();
    	criteriaQuery = builer.createQuery(Student.class);
    	root = criteriaQuery.from(Student.class);
    	criteriaQuery.where(
    			builer.and(builer.greaterThanOrEqualTo(root.get("id"), 1), builer.lessThanOrEqualTo(root.get("id"), 2))).select(root);
    	students = session.createQuery(criteriaQuery).list();
    	students.forEach(System.out::println);System.out.println();
    	
		// commit
		session.getTransaction().commit();
		session.close();
	}
    
    public static void readMultipleRoots(Session session) {	
    	
    	session.beginTransaction();
		
		// Criteria - Tuple
    	CriteriaBuilder builer = session.getCriteriaBuilder();
    	CriteriaQuery<Tuple> criteriaQuery = builer.createQuery(Tuple.class);
    	Root<Student> studetRoot = criteriaQuery.from(Student.class);
    	Root<Course> courseRoot = criteriaQuery.from(Course.class);
    	criteriaQuery.multiselect(studetRoot, courseRoot);
    	
    	Predicate studentRestriction = builer.lessThanOrEqualTo(studetRoot.get("id"), 2);
    	Predicate courseRestriction = builer.lessThanOrEqualTo(courseRoot.get("id"), 2);
    	
    	criteriaQuery.where(builer.and(studentRestriction, courseRestriction));
    	
    	List<Tuple> tupleList = session.createQuery(criteriaQuery).list();
    	for (Tuple tuple : tupleList) {
    		Student student = (Student) tuple.get(0);
    		Course course = (Course) tuple.get(1);
    		System.out.println(student + ", " + course);
		}
    	System.out.println();
    	
		// commit
		session.getTransaction().commit();
		session.close();
	}
    
    // *Below methods are deprecated after HIBERNATE v5
    public static void readOld(Session session) {	
    	
    	session.beginTransaction();
		
		// Criteria - query all records
    	Criteria criteria = session.createCriteria(Student.class).addOrder(Order.desc("age"));
		List<Student> results = (List<Student>) criteria.list();
		System.out.println(results.size());System.out.println();
		
		// Criteria - query by Primary Key - single result expected
		criteria = session.createCriteria(Student.class);
		criteria.add(Restrictions.eq("id", 1));
		Student student = (Student) criteria.uniqueResult();
		System.out.println(student);System.out.println();
		
		// Criteria - query with or condition
		criteria = session.createCriteria(Student.class);
		criteria.add(
				Restrictions.or(Restrictions.eq("id", 1), Restrictions.eq("id", 2))
				);
		results = (List<Student>) criteria.list();
		System.out.println(results.size());System.out.println();
		
		// Criteria - projection - select specific column
		criteria = session.createCriteria(Student.class);
		criteria.setProjection(Projections.property("id")).addOrder(Order.desc("id"));
		List<Integer> idList = (List<Integer>) criteria.list();
		System.out.println("ID List: " + idList);System.out.println();
		
		// Criteria - projection - max
		criteria = session.createCriteria(Student.class);
		criteria.setProjection(Projections.max("age"));
		int maxAge = (Integer) criteria.uniqueResult();
		System.out.println("Maximum age is: " + maxAge);System.out.println();
		
		// Criteria - query with Example (HIBERNATE will ignore null and primary key columns by default)
		student = new Student();
		student.setAge(10);
		Example example = Example.create(student);
		criteria = session.createCriteria(Student.class);
		criteria.add(example);
		results = (List<Student>) criteria.list();
		System.out.println("Example: " + results.size());System.out.println();
		
		// commit
		session.getTransaction().commit();
		session.close();
	}
    
}
