package sg.com.crimsonlogic.hibernate.dto;

public class StudentDTO {

	private int id;
	private String firstName;
	
	public StudentDTO(int id, String firstName) {
		super();
		this.id = id;
		this.firstName = firstName;
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

	@Override
	public String toString() {
		return "StudentDTO [id=" + id + ", firstName=" + firstName + "]";
	}
	
	
}
