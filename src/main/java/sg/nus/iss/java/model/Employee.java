package sg.nus.iss.java.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;

@Entity
@DiscriminatorValue("Employee")
public class Employee extends User{
	
	@OneToMany (mappedBy = "employee", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Leave> leaves;
	
	@ManyToOne
	private Manager manager;
	
	@ManyToOne
	//@JsonIgnore
	private LeaveQuota leaveQuota;
	
	@OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
	private CompensationLedger compensationLedger;
	
	@OneToMany(mappedBy = "employee", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<CompensationClaim> claims;
	
	public Employee() {
		super();
		leaves = new ArrayList<>();
		compensationLedger = new CompensationLedger(this);
	}

	public Employee(String name, String username, String password) {
		super(name, username, password);
		leaves = new ArrayList<>();
		compensationLedger = new CompensationLedger(this);
	}
	
	public List<Leave> getLeaves() {
		return leaves;
	}

	public void setLeaves(List<Leave> leaves) {
		this.leaves = leaves;
	}
	
	public void addLeave(Leave leave) {
		leaves.add(leave);
	}

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}

	@Override
	public String toString() {
		return "Employee [" + this.getName() + ", manager: " + this.getManager().getName() + "]";
	}

	public LeaveQuota getLeaveQuota() {
		return leaveQuota;
	}

	public void setLeaveQuota(LeaveQuota leaveQuota) {
		this.leaveQuota = leaveQuota;
	}
	
	public CompensationLedger getCompensationLedger() {
		return compensationLedger;
	}

	public void setCompensationLedger(CompensationLedger compensationLedger) {
		this.compensationLedger = compensationLedger;
	}

	public List<CompensationClaim> getClaims() {
		return claims;
	}

	public void setClaims(List<CompensationClaim> claims) {
		this.claims = claims;
	}
	
	public double calculateLeaveBalByType(LeaveType type) {
		double quota = 0;
		System.out.println("type: " + type);
		switch (type){
			case Annual:
				quota = getLeaveQuota().getAnnualLeaveQuota();
				break;
			case Medical:
				quota = getLeaveQuota().getMedicalLeaveQuota();
				break;
			case Compensation:
				quota = compensationLedger.calculateApprovedCompensationClaims();
				break;
		}
		System.out.println("quota: " + quota);
		double leaveUsed = 0;
		for (Leave leave : leaves) {
			if (leave.getType() == type && (leave.getStatus() == Status.Applied || leave.getStatus() == Status.Approved || leave.getStatus() == Status.Updated)) {
				leaveUsed += leave.getDuration();
				System.out.println("leave counted: " + leave);
			}
			else
				System.out.println("leave NOT counted: " + leave);
		}
		if (type == LeaveType.Compensation) {
			leaveUsed = leaveUsed * 8;
		}
		System.out.println();
		return quota - leaveUsed;
	}

	public Leave getLeave(int id) {
		for (Leave leave : leaves) {
			if (leave.getId() == id) {
				return leave;
			}
		}
		return null;
	}


}
