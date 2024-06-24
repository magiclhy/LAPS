package sg.nus.iss.java.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class CompensationLedger {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@OneToOne
	private Employee employee;
	@OneToMany(mappedBy = "compensationLedger", fetch = FetchType.EAGER)
	private List<CompensationClaim> compensationClaims;
	@OneToMany(fetch = FetchType.EAGER)
	private List<Leave> compensationLeave;
	
	public CompensationLedger () {
		compensationClaims = new ArrayList<>();
		setCompensationLeave(new ArrayList<>());
	}
	
	public CompensationLedger (Employee employee) {
		this.setEmployee(employee);
		compensationClaims = new ArrayList<>();
		setCompensationLeave(new ArrayList<>());
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public List<CompensationClaim> getCompensationClaims() {
		return compensationClaims;
	}

	public void setCompensationClaims(List<CompensationClaim> compensationClaims) {
		this.compensationClaims = compensationClaims;
	}

	public List<Leave> getCompensationLeave() {
		return compensationLeave;
	}

	public void setCompensationLeave(List<Leave> compensationLeave) {
		this.compensationLeave = compensationLeave;
	}
	
	public double calculateApprovedCompensationClaims() {
		double hoursClaimable = 0;
		for (CompensationClaim claim : compensationClaims) {
			if (claim.getStatus() == Status.Approved) {
				hoursClaimable += claim.getHours();
			}
		}
//		double daysClaimable = hoursClaimable/8;
//		//Round down to nearest half since granularity is half day
//		double daysClaimableRounded = Math.floor(daysClaimable * 2) / 2;
		return hoursClaimable;
	}
}
