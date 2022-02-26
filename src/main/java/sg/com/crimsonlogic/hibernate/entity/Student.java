package sg.com.crimsonlogic.hibernate.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "student")
@NamedQuery(name = "student.id", query = "from Student where id=:id")
@NamedNativeQuery(name = "student.id.native", query = "select * from Student where id=:id", resultClass = Student.class)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(nullable = false, name = "first_name")
	private String firstName;
	
	@Column(nullable = false, name = "last_name")
	private String lastName;
	
	@Column(name = "age")
	private int age;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = {
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
	})
	@JoinTable(name = "course_student",
		joinColumns = @JoinColumn(name = "student_id"),
		inverseJoinColumns = @JoinColumn(name = "course_id")
	)
	private Set<Course> courses;
	
	public Student() {
	}
	
	public Student(String firstName, String lastName, int age) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	public Set<Course> getCourses() {
		return courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", age=" + age + "]";
	}
	
	public void addCourse(Course course) {
		
		if(courses == null) {
			courses = new HashSet<Course>();
		}
		courses.add(course);
	}
	
	public void removeCourse(Course course) {
		
		if(courses == null) {
			courses = new HashSet<Course>();
		}
		courses.remove(course);
	}
	
	
}
