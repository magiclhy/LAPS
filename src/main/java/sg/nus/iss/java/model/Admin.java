package sg.nus.iss.java.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Admin")
public class Admin extends User {
	
	public Admin() {
		super();
	}
	
	public Admin(String name, String username, String password) {
		super(name, username, password);
	}
}
