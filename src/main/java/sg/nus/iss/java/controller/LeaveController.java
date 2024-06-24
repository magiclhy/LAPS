package sg.nus.iss.java.controller;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

@Controller
@RequestMapping("leaves")
public class LeaveController {
	
	@Autowired
	private LeaveService leaveService;
	
	@Autowired
	private PublicHolidayService publicHolidayService;
	
	//EMPLOYEE CREATE LEAVE
	@GetMapping("/create")
	public String createNewLeave(Model model, HttpSession sessionObj) {
		Leave leave = new Leave();
		leave.setDuration(1); //Set duration to 1 as daterangepicker default date is today-today
		model.addAttribute("leave", leave);
		LocalDate startDate = LocalDate.now();
		LocalDate endDate = LocalDate.now();
		model.addAttribute("startDate", startDate.toString());
		model.addAttribute("endDate", endDate.toString());
		List<PublicHoliday> publicHolidays = publicHolidayService.findAllPublicHolidays();
		model.addAttribute("publicHolidays", publicHolidays);

		Employee employee = (Employee) sessionObj.getAttribute("user");
		String Role = (String) sessionObj.getAttribute("role");
		model.addAttribute("role", Role);
		double annualLeaveBal = employee.calculateLeaveBalByType(LeaveType.Annual);
		sessionObj.setAttribute("annualLeaveBal", annualLeaveBal);
		double medicalLeaveBal = employee.calculateLeaveBalByType(LeaveType.Medical);
		sessionObj.setAttribute("medicalLeaveBal", medicalLeaveBal);
		double compensationLeaveBalHours = employee.calculateLeaveBalByType(LeaveType.Compensation);
		sessionObj.setAttribute("compensationLeaveBalHours", compensationLeaveBalHours);
		//calculate in terms of Days truncate to 2 dp
		double compensationLeaveBalDays = Math.floor(compensationLeaveBalHours/8 * 100) / 100;
		sessionObj.setAttribute("compensationLeaveBalDays", compensationLeaveBalDays);
		return "createLeave";
	}
	
	@PostMapping("/new")
	public String submitNewLeave(@Valid @ModelAttribute Leave leave, BindingResult bindingResult,
			Model model, @RequestParam("type") LeaveType type, HttpSession sessionObj) {
		if (bindingResult.hasErrors()) {
			System.out.println("Error binding :(");
			leave.setDuration(1);
	        LocalDate startDate = LocalDate.now();
	        LocalDate endDate = LocalDate.now();
			model.addAttribute("startDate", startDate.toString());
			model.addAttribute("endDate", endDate.toString());
			List<PublicHoliday> publicHolidays = publicHolidayService.findAllPublicHolidays();
			model.addAttribute("publicHolidays", publicHolidays);
			String Role = (String) sessionObj.getAttribute("role");
			model.addAttribute("role", Role);
			return "createLeave";
		}
		
		//Get employee object from session and save to leave
		Employee employee = (Employee) sessionObj.getAttribute("user");
		//CEO's leave request will be automatically approved
		leave.setEmployee(employee);
		if (leave.getEmployee().getType().equals("Ceo")) {
			leave.setStatus(Status.Approved);
		}
		
		//Set type of leave
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
		//If user is satisfied with daterangepicker default dates and did not select,
		//both startDate and endDate are saved as null, so need to set
		if (leave.getStartDate() == null) {
			leave.setStartDate(LocalDate.now());
			leave.setEndDate(LocalDate.now());
		}
		leaveService.saveLeave(leave);
		//reset the session user object so that it is updated
		employee.getLeaves().add(leave);
		sessionObj.setAttribute("user", employee);
		return "redirect:/leaves/view";
	}
	
	//EMPLOYEE VIEW PERSONAL LEAVE RECORDS
	@GetMapping("/view")
	public String viewLeaves(Model model, HttpSession sessionObj,
			@RequestParam(name = "pageNo", defaultValue = "0") int pageNo, @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
		Employee employee = (Employee) sessionObj.getAttribute("user");
		int id = employee.getId();
		System.out.println("pageSize: " + pageSize);
		Page<Leave> leavePage = leaveService.findAllLeavesByPage(id, pageNo, pageSize);
		model.addAttribute("leaves", leavePage.getContent());
		model.addAttribute("currentPage", leavePage.getNumber());
		model.addAttribute("totalPages", leavePage.getTotalPages());
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("today", LocalDate.now());
		return "viewLeave";
	}
	
	//EMPLOYEE UPDATE LEAVE
	@GetMapping("/edit/{id}")
	public String editLeave(Model model, @PathVariable("id") Integer id, HttpSession sessionObj) {
		Optional<Leave> optLeave = leaveService.findLeave(id);
		if (optLeave.isPresent()) {
			Leave leave = optLeave.get();
			model.addAttribute(leave);
			LocalDate startDate = leave.getStartDate();
			LocalDate endDate = leave.getEndDate();
			model.addAttribute("startDate", startDate.toString());
			model.addAttribute("endDate", endDate.toString());
			Employee employee = (Employee) sessionObj.getAttribute("user");
			List<PublicHoliday> publicHolidays = publicHolidayService.findAllPublicHolidays();
			model.addAttribute("publicHolidays", publicHolidays);
			
			//Show leave balance not including the leave being edited
			double leaveBal = employee.calculateLeaveBalByType(leave.getType()) + leave.getDuration();
			sessionObj.setAttribute("leaveBal", leaveBal);
		}
		return "editLeave";
	}
	
	@PostMapping("/save")
	public String saveLeave(@Valid @ModelAttribute Leave leave, BindingResult bindingResult,
			Model model, HttpSession sessionObj, @RequestParam("id") int id) {
		if (bindingResult.hasErrors()) {
			System.out.println("Error binding :(");
			if (leave.getStartDate() == null) {
				leave.setDuration(1);
			}
	        LocalDate startDate = (leave.getStartDate() != null) ? leave.getStartDate() : LocalDate.now();
	        LocalDate endDate = (leave.getEndDate() != null) ? leave.getEndDate() : LocalDate.now();
			model.addAttribute("startDate", startDate.toString());
			model.addAttribute("endDate", endDate.toString());
			List<PublicHoliday> publicHolidays = publicHolidayService.findAllPublicHolidays();
			model.addAttribute("publicHolidays", publicHolidays);
			return "editLeave";
		}
		//If user is satisfied with daterangepicker default dates and did not select,
		//both startDate and endDate are saved as null, so need to set
		if (leave.getStartDate() == null) {
			Leave originalLeave = leaveService.findLeave(id).get();
			leave.setStartDate(originalLeave.getStartDate());
			leave.setEndDate(originalLeave.getEndDate());
		}
		//Get employee object from session and save to leave
		Employee employee = (Employee) sessionObj.getAttribute("user");
		leave.setEmployee(employee);
		leave.setStatus(Status.Updated);
		leaveService.saveLeave(leave);
		//reset the session user object
		sessionObj.setAttribute("user", employee);
		return "redirect:/leaves/view";
	}
	
	//EMPLOYEE DELETE LEAVE
	@GetMapping("/delete/{id}")
	public String deleteLeave(@PathVariable("id") Integer id, HttpSession sessionObj) {
		Optional<Leave> optLeave = leaveService.findLeave(id);
		if (optLeave.isPresent()) {
			Leave leave = optLeave.get();
			leave.setStatus(Status.Deleted);
			leaveService.saveLeave(leave);
			//reset the session user object
			Employee employee = leave.getEmployee();
			sessionObj.setAttribute("user", employee);
		}
		return "redirect:/leaves/view";
	}
	
	//EMPLOYEE CANCEL LEAVE
	@GetMapping("/cancel/{id}")
	public String cancelLeave(Model model, @PathVariable("id") Integer id, HttpSession sessionObj) {
		Optional<Leave> optLeave = leaveService.findLeave(id);
		if (optLeave.isPresent()) {
			Leave leave = optLeave.get();
			model.addAttribute(leave);
		}
		return "viewDetailsCancel";
	}
	
	@GetMapping("/confirmCancel/{id}")
	public String confirmCancelLeave(@PathVariable("id") Integer id,  HttpSession sessionObj) {
		Optional<Leave> optLeave = leaveService.findLeave(id);
		if (optLeave.isPresent()) {
			Leave leave = optLeave.get();
			leave.setStatus(Status.Canceled);
			leaveService.saveLeave(leave);
			//reset the session user object
			Employee employee = leave.getEmployee();
			sessionObj.setAttribute("user", employee);
		}
		return "redirect:/leaves/view";
	}
	

	//MANAGER APPROVE/REJECT LEAVE
	@GetMapping("/viewLeaveForApproval")
	public String processLeaves(Model model, HttpSession sessionObj) {
		Employee employee = (Employee) sessionObj.getAttribute("user");
		int id = employee.getId();
		if (sessionObj.getAttribute("role").equals("Manager")) {
			List<Leave> leaves = leaveService.findEmpLeavesForApproval(id);
			model.addAttribute("leaves", leaves);
		}
		else if (sessionObj.getAttribute("role").equals("Ceo")) {
			List<Leave> leaves = leaveService.findManLeavesForApproval(id);
			model.addAttribute("leaves", leaves);
		}
		return "viewLeaveForApproval";
	}
	
	@GetMapping("/viewDetails/{id}")
	public String viewDetails (Model model, @PathVariable("id") Integer id, HttpSession sessionObj) {
		Optional<Leave> optLeave = leaveService.findLeave(id);
		if (optLeave.isPresent()) {
			Leave leave = optLeave.get();
			model.addAttribute("leave", leave);
			Employee employee = (Employee) sessionObj.getAttribute("user");
			int managerId = employee.getId();
			List<Leave> otherLeaves = leaveService.findAllLeavesDuringLeavePeriod(managerId, leave.getStartDate(), leave.getEndDate());
			model.addAttribute("otherLeaves", otherLeaves);
		}
		return "viewDetails";
	}
	
	@PostMapping("/confirmOutcome/{id}")
	public String confirmOutcome (@RequestParam("comment") String comment, @RequestParam("applicationOutcome") String applicationOutcome, @PathVariable("id") Integer id) {
		Optional<Leave> optLeave = leaveService.findLeave(id);
		if (optLeave.isPresent()) {
			Leave leave = optLeave.get();
			leave.setComment(comment);
			if (applicationOutcome.equals("Approve")) {
				leave.setStatus(Status.Approved);
			}
			else if (applicationOutcome.equals("Reject")) {
				leave.setStatus(Status.Rejected);
			}
			leaveService.saveLeave(leave);
		}
		return "redirect:/leaves/viewLeaveForApproval";
	}
	
	//MANAGER VIEW SUBORDINATE LEAVES
	@GetMapping("/viewSubLeaves")
	public String viewSubLeaves(Model model, HttpSession sessionObj, 
			@RequestParam(name = "pageNo", defaultValue = "0") int pageNo, @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
		Employee employee = (Employee) sessionObj.getAttribute("user");
		int id = employee.getId();
		if (sessionObj.getAttribute("role").equals("Manager")) {
			Page<Leave> leavePage = leaveService.findAllLeavesOfEmpSub(id, pageNo, pageSize);
			model.addAttribute("leaves", leavePage.getContent());
			model.addAttribute("currentPage", leavePage.getNumber());
			model.addAttribute("totalPages", leavePage.getTotalPages());
			model.addAttribute("pageSize", pageSize);
		}
		else if (sessionObj.getAttribute("role").equals("Ceo")) {
			Page<Leave> leavePage = leaveService.findAllLeavesOfManSub(id, pageNo, pageSize);
			model.addAttribute("leaves", leavePage.getContent());
			model.addAttribute("currentPage", leavePage.getNumber());
			model.addAttribute("totalPages", leavePage.getTotalPages());
			model.addAttribute("pageSize", pageSize);
		}
		
		return "viewSubLeave";
	}
	
	@GetMapping("/viewDetailsSub/{id}")
	public String viewDetailsSub (Model model, @PathVariable("id") Integer id, HttpSession sessionObj) {
		Optional<Leave> optLeave = leaveService.findLeave(id);
		if (optLeave.isPresent()) {
			Leave leave = optLeave.get();
			model.addAttribute("leave", leave);
		}
		return "viewDetailsSub";
	}
	
	//MOVEMENT REGISTER
	@GetMapping("/movementRegister")
	public String viewMovementRegister (Model model) {
		LocalDate currentDate = LocalDate.now();
		Month currentMonth = currentDate.getMonth();
		Month previousMonth = currentMonth.minus(1);
		Month nextMonth = currentMonth.plus(1);
		model.addAttribute("currentMonth", currentMonth);
		model.addAttribute("previousMonth", previousMonth);
		model.addAttribute("nextMonth", nextMonth);
		
		List<Leave> currentMonthLeaves = leaveService.findAllLeavesCurrMonth();
		List<Leave> previousMonthLeaves = leaveService.findAllLeavesPrevMonth();
		List<Leave> nextMonthLeaves = leaveService.findAllLeavesNextMonth();
		model.addAttribute("currentMonthLeaves", currentMonthLeaves);
		model.addAttribute("previousMonthLeaves", previousMonthLeaves);
		model.addAttribute("nextMonthLeaves", nextMonthLeaves);
		return "movementRegister";
	}
}
