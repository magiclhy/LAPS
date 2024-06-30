package sg.nus.iss.java.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sg.nus.iss.java.model.CompensationClaim;
import sg.nus.iss.java.model.Employee;
import sg.nus.iss.java.model.Leave;
import sg.nus.iss.java.service.CompensationClaimService;
import sg.nus.iss.java.service.ExportCSV;
import sg.nus.iss.java.service.LeaveService;

@Controller
@RequestMapping("report")
public class ReportController {

	@Autowired
	private LeaveService leaveService;
	
	@Autowired
	private CompensationClaimService compensationClaimService;
	
	private final ExportCSV csvExportService;
	
	public ReportController(ExportCSV csvExportService) {
		this.csvExportService = csvExportService;
	}
	
	//VIEW REPORT (Employee on leave during a selected period)
	@GetMapping("/searchLeavePeriod")
	public String searchLeavePeriod() {
		return "searchLeavePeriod";
	}
	
	@PostMapping("/searchLeavePeriod")
	public String viewResults(@RequestParam("startDate") String startDateStr, @RequestParam("endDate") String endDateStr, 
			Model model, HttpSession sessionObj) {
		//convert to LocalDate from string since input type was text
		DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate startDate = LocalDate.parse(startDateStr, df);
		LocalDate endDate = LocalDate.parse(endDateStr, df);
		
		//Get manager id from session 
		Employee employee = (Employee) sessionObj.getAttribute("user");
		int managerId = employee.getId();
		List<Leave> leaves = leaveService.findAllLeavesDuringLeavePeriod(managerId, startDate, endDate);
		model.addAttribute("leaves", leaves);
		model.addAttribute("startDateSelected", startDateStr);
		model.addAttribute("endDateSelected", endDateStr);
		
		//Add to session in case want to export
		sessionObj.setAttribute("startDateSelected", startDate);
		sessionObj.setAttribute("endDateSelected", endDate);
		sessionObj.setAttribute("listToExport", leaves);
		return "searchResultsLeavePeriod";
	}
	
	//EXPORT REPORT TO CSV (Employee on leave during a selected period)
	@GetMapping(path = "/exportLeaves")
    public void exportSearchResultsLeavePeriodInCsv(HttpServletResponse servletResponse, HttpSession sessionObj) throws IOException {
        LocalDate startDate = (LocalDate) sessionObj.getAttribute("startDateSelected");
        LocalDate endDate = (LocalDate) sessionObj.getAttribute("endDateSelected");
        List<Leave> leaves = (List<Leave>) sessionObj.getAttribute("listToExport");
		
		servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition","attachment; filename=\"leaves.csv\"");
        csvExportService.writeLeavesToCsv(servletResponse.getWriter(), leaves, startDate, endDate);
    }
	
	//VIEW REPORT (Compensation claims for all/particular employee)
	@GetMapping("/searchClaims")
	public String searchClaims() {
		return "searchClaims";
	}
	
	@PostMapping("/searchClaims")
	public String searchResultsClaims(@RequestParam("name") String name, HttpSession sessionObj, Model model) {
		//Get manager id from session 
		Employee employee = (Employee) sessionObj.getAttribute("user");
		int supId = employee.getId();
		if (sessionObj.getAttribute("role").equals("Manager")) {
			List<CompensationClaim> claims = compensationClaimService.findAllEmpClaimsByName(supId, name);
			model.addAttribute("claims", claims);
			sessionObj.setAttribute("listToExport", claims);
		}
		else if (sessionObj.getAttribute("role").equals("Ceo")) {
			List<CompensationClaim> claims = compensationClaimService.findAllManClaimsByName(supId, name);
			model.addAttribute("claims", claims);
			sessionObj.setAttribute("listToExport", claims);
		}
		return "searchResultsClaims";
	}
	
	@GetMapping(path = "/exportClaims")
    public void exportSearchResultsClaimsInCsv(HttpServletResponse servletResponse, HttpSession sessionObj) throws IOException {
        List<CompensationClaim> claims = (List<CompensationClaim>) sessionObj.getAttribute("listToExport");
		
		servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition","attachment; filename=\"claims.csv\"");
        csvExportService.writeClaimsToCsv(servletResponse.getWriter(), claims);
    }
}
