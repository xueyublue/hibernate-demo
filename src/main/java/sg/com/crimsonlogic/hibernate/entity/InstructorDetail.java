package sg.com.crimsonlogic.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "instructor_detail")
public class InstructorDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "website")
	private String website;
	
	@Column(name = "hobby")
	private String hobby;
	
	// Add below for BI-directional one to one mapping
	// value of "mappedBy" should be the same filed name in Instructor.class
//	@OneToOne(mappedBy = "instructorDetail", cascade = {
//			CascadeType.ALL
//	})
//	private Instructor instructor;
	
	public InstructorDetail() {
	}
	
	public InstructorDetail(String website, String hobby) {
		this.website = website;
		this.hobby = hobby;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	@Override
	public String toString() {
		return "InstructorDetail [id=" + id + ", website=" + website + ", hobby=" + hobby + "]";
	}
	
	
}
