package sg.nus.iss.java.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;

@Entity
@DiscriminatorValue("Manager")
public class Manager extends Employee {
		
	@OneToMany(mappedBy="manager")
	private List<Employee> employees;
	
	public Manager() {
		super();
		employees = new ArrayList<>();
	}
	
	public Manager(String name, String username, String password) {
		super(name, username, password);
		employees = new ArrayList<>();
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

}
