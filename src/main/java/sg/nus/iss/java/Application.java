package sg.nus.iss.java;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import sg.nus.iss.java.model.Admin;
import sg.nus.iss.java.model.Ceo;
import sg.nus.iss.java.model.CompensationClaim;
import sg.nus.iss.java.model.CompensationLedger;
import sg.nus.iss.java.model.Employee;
import sg.nus.iss.java.model.Leave;
import sg.nus.iss.java.model.LeaveQuota;
import sg.nus.iss.java.model.LeaveType;
import sg.nus.iss.java.model.Manager;
import sg.nus.iss.java.model.PublicHoliday;
import sg.nus.iss.java.model.Status;
import sg.nus.iss.java.repository.AdminRepository;
import sg.nus.iss.java.repository.CeoRepository;
import sg.nus.iss.java.repository.CompensationClaimRepository;
import sg.nus.iss.java.repository.CompensationLedgerRepository;
import sg.nus.iss.java.repository.EmployeeRepository;
import sg.nus.iss.java.repository.LeaveQuotaRepository;
import sg.nus.iss.java.repository.LeaveRepository;
import sg.nus.iss.java.repository.ManagerRepository;
import sg.nus.iss.java.repository.PublicHolidayRepository;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public CommandLineRunner commandLineRun(EmployeeRepository employeeRepository, LeaveRepository leaveRepository, 
			AdminRepository adminRepository, ManagerRepository managerRepository, CeoRepository ceoRepository, PublicHolidayRepository publicHolidayRepository, 
			LeaveQuotaRepository leaveQuotaRepository, CompensationLedgerRepository compensationLedgerRepository, 
			CompensationClaimRepository compensationClaimRepository) {
		return args -> {
			Manager manager1 = managerRepository.save(new Manager("Paddington Bear", "manager1", "manager1"));
			Manager manager2 = managerRepository.save(new Manager("Agatha Christie", "manager2", "manager2"));
			Employee employee1 = employeeRepository.save(new Employee("Hercule Poirot", "employee1", "employee1"));
			Employee employee2 = employeeRepository.save(new Employee("Sherlock Holmes", "employee2", "employee2"));
			Employee employee3 = employeeRepository.save(new Employee("Miss Marple", "employee3", "employee3"));
			Ceo ceo = ceoRepository.save(new Ceo("Jay Gatsby", "ceo", "ceo"));
			Admin admin1 = adminRepository.save(new Admin("Dr Watson", "admin1", "admin1"));
			employee1.setManager(manager1);
			employee2.setManager(manager1);
			employee3.setManager(manager1);
			manager1.setCeo(ceo);
			manager2.setCeo(ceo);
			
			LeaveQuota leaveQuota1 = new LeaveQuota("Administrative" ,"2024", "Manager");
			leaveQuota1.setAnnualLeaveQuota(14);
			leaveQuota1.setMedicalLeaveQuota(60);
			leaveQuotaRepository.save(leaveQuota1);
			
			LeaveQuota leaveQuota2 = new LeaveQuota("Professional", "2024", "Employee");
			leaveQuota2.setAnnualLeaveQuota(18);
			leaveQuota2.setMedicalLeaveQuota(60);
			leaveQuotaRepository.save(leaveQuota2);
			
			LeaveQuota leaveQuota3 = new LeaveQuota("Ceo", "2024", "Ceo");
			leaveQuota2.setAnnualLeaveQuota(365);
			leaveQuota2.setMedicalLeaveQuota(365);
			leaveQuotaRepository.save(leaveQuota3);
			
			manager1.setLeaveQuota(leaveQuota1);
			manager2.setLeaveQuota(leaveQuota1);
			employee1.setLeaveQuota(leaveQuota2);
			employee2.setLeaveQuota(leaveQuota2);
			employee3.setLeaveQuota(leaveQuota2);
			ceo.setLeaveQuota(leaveQuota3);
			
			managerRepository.save(manager1);
			managerRepository.save(manager2);
			
			employeeRepository.save(employee1);
			employeeRepository.save(employee2);
			employeeRepository.save(employee3);
			
			ceoRepository.save(ceo);
			
			DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate date1 = LocalDate.parse("01/05/2024", df);
			LocalDate date2 = LocalDate.parse("05/05/2024", df);
			LocalDate date3 = LocalDate.parse("08/05/2024", df);
			LocalDate date4 = LocalDate.parse("09/05/2024", df);
			LocalDate date5 = LocalDate.parse("24/06/2024", df);
			LocalDate date6 = LocalDate.parse("27/06/2024", df);
			LocalDate date7 = LocalDate.parse("15/01/2024", df);
			LocalDate date8 = LocalDate.parse("16/01/2024", df);
			LocalDate date9 = LocalDate.parse("13/07/2024", df);
			LocalDate date10 = LocalDate.parse("15/07/2024", df);
			LocalDate date11 = LocalDate.parse("26/07/2024", df);
			LocalDate date12 = LocalDate.parse("27/07/2024", df);
			Leave annualLeave1 = leaveRepository.save(new Leave(date1, date2, "Holidays", LeaveType.Annual));
			Leave compensationLeave1 = leaveRepository.save(new Leave(date5, date6, "Overtime", LeaveType.Compensation));
			Leave medicalLeave1 = leaveRepository.save(new Leave(date3, date4, "Check up", LeaveType.Medical));
			Leave annualLeave2 = leaveRepository.save(new Leave(date7, date8, "Break", LeaveType.Annual));
			Leave annualLeave3 = leaveRepository.save(new Leave(date1, date4, "Going overseas", LeaveType.Annual));
			Leave compensationLeave2 = leaveRepository.save(new Leave(date2, date4, "Overtime", LeaveType.Compensation));
			Leave medicalLeave2 = leaveRepository.save(new Leave(date5, date6, "Medical Appt", LeaveType.Medical));
			Leave medicalLeave3 = leaveRepository.save(new Leave(date11, date12, "Sick", LeaveType.Medical));
			Leave annualLeave4 = leaveRepository.save(new Leave(date9, date10, "Going overseas", LeaveType.Annual));
			
			annualLeave1.setEmployee(employee1);
			compensationLeave1.setEmployee(employee1);
			medicalLeave1.setEmployee(employee1);
			annualLeave2.setEmployee(employee1);
			annualLeave3.setEmployee(employee3);
			compensationLeave2.setEmployee(employee2);
			medicalLeave2.setEmployee(employee3);
			medicalLeave3.setEmployee(employee1);
			annualLeave4.setEmployee(employee1);
			
			annualLeave1.setDuration(5);
			compensationLeave1.setDuration(3.5);
			medicalLeave1.setDuration(2);
			annualLeave2.setDuration(2);
			annualLeave3.setDuration(7);
			compensationLeave2.setDuration(5);
			medicalLeave2.setDuration(4);
			medicalLeave3.setDuration(2);
			annualLeave4.setDuration(3);
			
			annualLeave1.setStatus(Status.Approved);
			annualLeave2.setStatus(Status.Approved);
			compensationLeave1.setStatus(Status.Rejected);
			annualLeave3.setStatus(Status.Approved);
			compensationLeave2.setStatus(Status.Rejected);
			medicalLeave1.setStatus(Status.Approved);
			medicalLeave2.setStatus(Status.Approved);
			medicalLeave3.setStatus(Status.Applied);
			annualLeave4.setStatus(Status.Updated);
			
			leaveRepository.save(annualLeave1);
			leaveRepository.save(compensationLeave1);
			leaveRepository.save(medicalLeave1);
			leaveRepository.save(annualLeave2);
			leaveRepository.save(annualLeave3);
			leaveRepository.save(compensationLeave2);
			leaveRepository.save(medicalLeave2);
			leaveRepository.save(medicalLeave3);
			leaveRepository.save(annualLeave4);
			
			
			PublicHoliday PH1 = new PublicHoliday("New Year's Day", LocalDate.parse("01/01/2024", df));
			PublicHoliday PH2 = new PublicHoliday("Chinese New Year Day 1", LocalDate.parse("10/02/2024", df));
			PublicHoliday PH3 = new PublicHoliday("Chinese New Year Day 2", LocalDate.parse("11/02/2024", df));
			PublicHoliday PH4 = new PublicHoliday("Good Friday", LocalDate.parse("29/03/2024", df));
			publicHolidayRepository.save(PH1);
			publicHolidayRepository.save(PH2);
			publicHolidayRepository.save(PH3);
			publicHolidayRepository.save(PH4);
			
			LocalTime time1 =  LocalTime.parse("08:20");
			LocalTime time2 =  LocalTime.parse("09:40");
			LocalTime time3 =  LocalTime.parse("18:20");
			LocalTime time4 =  LocalTime.parse("19:50");
			
			CompensationClaim claim1 = new CompensationClaim(date8, time1, time2, 1.25, employee1);
			CompensationClaim claim2 = new CompensationClaim(date4, time3, time4, 1.5, employee2);
			CompensationClaim claim3 = new CompensationClaim(date5, time1, time2, 1.25, employee1);
			CompensationClaim claim4 = new CompensationClaim(date7, time3, time4, 1.5, employee2);
			
			claim3.setStatus(Status.Approved);
			claim4.setStatus(Status.Approved);
			
			compensationClaimRepository.save(claim1);
			compensationClaimRepository.save(claim2);
			compensationClaimRepository.save(claim3);
			compensationClaimRepository.save(claim4);
		};
	}

}
