package sg.com.crimsonlogic.hibernate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import sg.com.crimsonlogic.hibernate.entity.Student;

public class Util {

	// create session factory
    public static SessionFactory getSessionFactory() {
		return new Configuration()
				.configure("hibernate.cfg.xml")
				.addAnnotatedClass(Student.class)
				.buildSessionFactory();
	}
    
    // logging level
    public static enum LEVEL {
		DEBUG, INFO, ERROR,
	}
	
    //logging
	public static void logging(LEVEL level, String message) {
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		
		StringBuilder output = new StringBuilder();
		
		output.append(simpleDateFormat.format(new Date())).append(" ");
		output.append(level.name()).append(" ");
		output.append(message);
		
		System.out.println(output.toString());
	}
	
	public static int randomStudentAge() {
		return ThreadLocalRandom.current().nextInt(10, 18);
	}
}
