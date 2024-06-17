package sg.nus.iss.java.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;

@Entity
public class LeaveQuota {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int annualLeaveQuota;
	private int medicalLeaveQuota;
	@Column(name = "leave_year")
	@Size (min = 4, max = 4, message = "Year should be 4 characters long.")
	private String year;
	private String role;
	
	@OneToMany(mappedBy = "leaveQuota")
	@JsonIgnore
	private List<Employee> employees;
	
	public LeaveQuota() {
	}
	
	public LeaveQuota(String year, String role) {
		this.year = year;
		this.role = role;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getAnnualLeaveQuota() {
		return annualLeaveQuota;
	}

	public void setAnnualLeaveQuota(int annualLeaveQuota) {
		this.annualLeaveQuota = annualLeaveQuota;
	}

	public int getMedicalLeaveQuota() {
		return medicalLeaveQuota;
	}

	public void setMedicalLeaveQuota(int medicalLeaveQuota) {
		this.medicalLeaveQuota = medicalLeaveQuota;
	}
}
