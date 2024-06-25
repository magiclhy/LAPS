package sg.nus.iss.java.api;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import sg.nus.iss.java.model.Ceo;
import sg.nus.iss.java.model.Employee;
import sg.nus.iss.java.model.Leave;
import sg.nus.iss.java.model.LeaveType;
import sg.nus.iss.java.model.PublicHoliday;
import sg.nus.iss.java.model.Status;
import sg.nus.iss.java.service.LeaveService;
import sg.nus.iss.java.service.PublicHolidayService;

@RestController
@RequestMapping("/api/leaves")
public class LeaveRestController {
	
	@Autowired
	private LeaveService leaveService;
	
	@Autowired
	private PublicHolidayService publicHolidayService;
	
	// EMPLOYEE CREATE LEAVE
	@PostMapping("/create")
	public ResponseEntity<Leave> createNewLeave(HttpSession sessionObj) {
		Leave leave = new Leave();
		leave.setDuration(1); // Set duration to 1 as daterangepicker default date is today-today
		
		Employee employee = (Employee) sessionObj.getAttribute("user");
		String Role = (String) sessionObj.getAttribute("role");

		double annualLeaveBal = employee.calculateLeaveBalByType(LeaveType.Annual);
		double medicalLeaveBal = employee.calculateLeaveBalByType(LeaveType.Medical);
		double compensationLeaveBalHours = employee.calculateLeaveBalByType(LeaveType.Compensation);
		double compensationLeaveBalDays = Math.floor(compensationLeaveBalHours / 8 * 100) / 100;

		sessionObj.setAttribute("annualLeaveBal", annualLeaveBal);
		sessionObj.setAttribute("medicalLeaveBal", medicalLeaveBal);
		sessionObj.setAttribute("compensationLeaveBalHours", compensationLeaveBalHours);
		sessionObj.setAttribute("compensationLeaveBalDays", compensationLeaveBalDays);

		return new ResponseEntity<>(leave, HttpStatus.CREATED);
	}
	
	@PostMapping("/new")
	public ResponseEntity<Leave> submitNewLeave(@Valid @RequestBody Leave leave, BindingResult bindingResult,
												HttpSession sessionObj, @RequestParam("type") LeaveType type) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Employee employee = (Employee) sessionObj.getAttribute("user");
		leave.setEmployee(employee);
		if (leave.getEmployee().getType().equals("Ceo")) {
			leave.setStatus(Status.Approved);
		}
		
		switch (type) {
			case Annual:
				leave.setType(LeaveType.Annual);
				break;
			case Medical:
				leave.setType(LeaveType.Medical);
				break;
			case Compensation:
				leave.setType(LeaveType.Compensation);
				employee.getCompensationLedger().getCompensationLeave().add(leave);
				break;
		}

		if (leave.getStartDate() == null) {
			leave.setStartDate(LocalDate.now());
			leave.setEndDate(LocalDate.now());
		}

		leaveService.saveLeave(leave);
		employee.getLeaves().add(leave);
		sessionObj.setAttribute("user", employee);
		return new ResponseEntity<>(leave, HttpStatus.CREATED);
	}
	
	// EMPLOYEE VIEW PERSONAL LEAVE RECORDS
	@GetMapping("/view")
	public ResponseEntity<Page<Leave>> viewLeaves(HttpSession sessionObj,
												  @RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
												  @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
		Employee employee = (Employee) sessionObj.getAttribute("user");
		int id = employee.getId();
		Page<Leave> leavePage = leaveService.findAllLeavesByPage(id, pageNo, pageSize);
		return new ResponseEntity<>(leavePage, HttpStatus.OK);
	}
	
	// EMPLOYEE UPDATE LEAVE
	@GetMapping("/edit/{id}")
	public ResponseEntity<Leave> editLeave(@PathVariable("id") Integer id, HttpSession sessionObj) {
		Optional<Leave> optLeave = leaveService.findLeave(id);
		if (optLeave.isPresent()) {
			Leave leave = optLeave.get();
			return new ResponseEntity<>(leave, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/save")
	public ResponseEntity<Leave> saveLeave(@Valid @RequestBody Leave leave, BindingResult bindingResult,
										   HttpSession sessionObj, @RequestParam("id") int id) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (leave.getStartDate() == null) {
			Leave originalLeave = leaveService.findLeave(id).get();
			leave.setStartDate(originalLeave.getStartDate());
			leave.setEndDate(originalLeave.getEndDate());
		}

		Employee employee = (Employee) sessionObj.getAttribute("user");
		leave.setEmployee(employee);
		leave.setStatus(Status.Updated);
		leaveService.saveLeave(leave);
		sessionObj.setAttribute("user", employee);
		return new ResponseEntity<>(leave, HttpStatus.OK);
	}
	
	// EMPLOYEE DELETE LEAVE
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteLeave(@PathVariable("id") Integer id, HttpSession sessionObj) {
		Optional<Leave> optLeave = leaveService.findLeave(id);
		if (optLeave.isPresent()) {
			Leave leave = optLeave.get();
			leave.setStatus(Status.Deleted);
			leaveService.saveLeave(leave);
			Employee employee = leave.getEmployee();
			sessionObj.setAttribute("user", employee);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	// EMPLOYEE CANCEL LEAVE
	@PutMapping("/cancel/{id}")
	public ResponseEntity<Leave> cancelLeave(@PathVariable("id") Integer id, HttpSession sessionObj) {
		Optional<Leave> optLeave = leaveService.findLeave(id);
		if (optLeave.isPresent()) {
			Leave leave = optLeave.get();
			leave.setStatus(Status.Canceled);
			leaveService.saveLeave(leave);
			Employee employee = leave.getEmployee();
			sessionObj.setAttribute("user", employee);
			return new ResponseEntity<>(leave, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	// MANAGER APPROVE/REJECT LEAVE
	@GetMapping("/viewLeaveForApproval")
	public ResponseEntity<List<Leave>> processLeaves(HttpSession sessionObj) {
		Employee employee = (Employee) sessionObj.getAttribute("user");
		int id = employee.getId();
		List<Leave> leaves;
		if (sessionObj.getAttribute("role").equals("Manager")) {
			leaves = leaveService.findEmpLeavesForApproval(id);
		} else if (sessionObj.getAttribute("role").equals("Ceo")) {
			leaves = leaveService.findManLeavesForApproval(id);
		} else {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		return new ResponseEntity<>(leaves, HttpStatus.OK);
	}
	
	@GetMapping("/viewDetails/{id}")
	public ResponseEntity<Leave> viewDetails(@PathVariable("id") Integer id, HttpSession sessionObj) {
		Optional<Leave> optLeave = leaveService.findLeave(id);
		if (optLeave.isPresent()) {
			Leave leave = optLeave.get();
			return new ResponseEntity<>(leave, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/confirmOutcome/{id}")
	public ResponseEntity<Leave> confirmOutcome(@RequestParam("comment") String comment,
												@RequestParam("applicationOutcome") String applicationOutcome,
												@PathVariable("id") Integer id) {
		Optional<Leave> optLeave = leaveService.findLeave(id);
		if (optLeave.isPresent()) {
			Leave leave = optLeave.get();
			leave.setComment(comment);
			if (applicationOutcome.equals("Approve")) {
				leave.setStatus(Status.Approved);
			} else if (applicationOutcome.equals("Reject")) {
				leave.setStatus(Status.Rejected);
			}
			leaveService.saveLeave(leave);
			return new ResponseEntity<>(leave, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	

}
