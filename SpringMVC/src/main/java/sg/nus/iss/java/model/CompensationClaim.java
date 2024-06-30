package sg.nus.iss.java.model;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="compensation_claim")
public class CompensationClaim {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotNull (message = "Date must not be blank")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate date;
	@Column(name="start_time")
	@NotNull (message = "Time must not be blank")
	private LocalTime startTime;
	@NotNull (message = "Time must not be blank")
	@Column(name="end_time")
	private LocalTime endTime;
	private double hours;
	private String remarks;
	@ManyToOne
	private Employee employee;
	@Enumerated(EnumType.STRING)
	private Status status;
	@ManyToOne
	private CompensationLedger compensationLedger;
	
	public CompensationClaim() {
		status = Status.Applied;
	}
	
	public CompensationClaim(LocalDate date, LocalTime startTime, LocalTime endTime, double hours, Employee employee) {
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.hours = hours;
		this.employee = employee;
		status = Status.Applied;
		this.compensationLedger = employee.getCompensationLedger();
		if (this.compensationLedger != null) {
			this.compensationLedger.getCompensationClaims().add(this);
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public double getHours() {
		return hours;
	}

	public void setHours(double hours) {
		this.hours = hours;
	}
	
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public CompensationLedger getCompensationLedger() {
		return compensationLedger;
	}

	public void setCompensationLedger(CompensationLedger compensationLedger) {
		this.compensationLedger = compensationLedger;
	}


}
