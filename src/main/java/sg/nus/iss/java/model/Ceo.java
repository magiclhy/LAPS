package sg.nus.iss.java.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
@DiscriminatorValue("Ceo")
public class Ceo extends Employee{
	
	@OneToMany(mappedBy = "ceo")
	private List<Manager> managers;
	
	public Ceo() {
		super();
		managers = new ArrayList<>();
	}
	
	public Ceo(String name, String username, String password) {
		super(name, username, password);
		managers = new ArrayList<>();
	}

	public List<Manager> getManagers() {
		return managers;
	}

	public void setManagers(List<Manager> managers) {
		this.managers = managers;
	}

}
