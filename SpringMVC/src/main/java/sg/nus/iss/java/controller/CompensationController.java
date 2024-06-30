package sg.nus.iss.java.controller;

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
import sg.nus.iss.java.model.CompensationClaim;
import sg.nus.iss.java.model.CompensationLedger;
import sg.nus.iss.java.model.Employee;
import sg.nus.iss.java.model.Leave;
import sg.nus.iss.java.model.Status;
import sg.nus.iss.java.service.CompensationClaimService;

@Controller
@RequestMapping("compensation")
public class CompensationController {
	@Autowired
	public CompensationClaimService compensationClaimService;
	
	// EMPLOYEE ADD COMPENSATION CLAIM
	@GetMapping("/create")
	public String createCompensationClaim(Model model) {
		model.addAttribute("claim", new CompensationClaim());
		return "createClaim";
	}
	
	@PostMapping("/create")
	public String confirmCompensationClaim(@Valid @ModelAttribute("claim") CompensationClaim claim, BindingResult bindingResult, 
			Model model, HttpSession sessionbj) {
		if (bindingResult.hasErrors()) {
			System.out.println("Error binding :(");
			return "createClaim";
		}
		Employee employee = (Employee) sessionbj.getAttribute("user");
		claim.setEmployee(employee);
		CompensationLedger compensationLedger = employee.getCompensationLedger();
		claim.setCompensationLedger(compensationLedger);
		compensationClaimService.save(claim);
		return "redirect:/compensation/view";
	}
	
	//VIEW PERSONAL CLAIMS
	@GetMapping("/view")
	public String viewLeaves(Model model, HttpSession sessionObj) {
		Employee employee = (Employee) sessionObj.getAttribute("user");
		int id = employee.getId();
		List<CompensationClaim> claims = compensationClaimService.findAllClaimsById(id);
		model.addAttribute("claims", claims);
		return "viewClaim";
	}
	
	//DELETE CLAIMS
	@GetMapping("/details/{id}")
	public String editClaim(Model model, @PathVariable("id") Integer id, HttpSession sessionObj) {
		Optional<CompensationClaim> optClaim = compensationClaimService.findClaim(id);
		if (optClaim.isPresent()) {
			CompensationClaim claim = optClaim.get();
			model.addAttribute("claim", claim);
		}
		return "detailsClaim";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteClaim(@PathVariable("id") Integer id, HttpSession sessionObj) {
		Optional<CompensationClaim> optClaim = compensationClaimService.findClaim(id);
		if (optClaim.isPresent()) {
			CompensationClaim claim = optClaim.get();
			claim.setStatus(Status.Deleted);
			compensationClaimService.save(claim);
		}
		return "redirect:/compensation/view";
	}
	
	//MANAGER APPROVE/REJECT CLAIM
	@GetMapping("/viewClaimsForApproval")
	public String processClaims(Model model, HttpSession sessionObj) {
		Employee employee = (Employee) sessionObj.getAttribute("user");
		int id = employee.getId();
		if (sessionObj.getAttribute("role").equals("Manager")) {
			List<CompensationClaim> claims = compensationClaimService.findEmpClaimsForApproval(id);
			model.addAttribute("claims", claims);
		}
		else if (sessionObj.getAttribute("role").equals("Ceo")) {
			List<CompensationClaim> claims = compensationClaimService.findManClaimsForApproval(id);
			model.addAttribute("claims", claims);
		}
		return "viewClaimForApproval";
	}
	
	@GetMapping("/viewDetails/{id}")
	public String viewDetails (Model model, @PathVariable("id") Integer id, HttpSession sessionObj) {
		Optional<CompensationClaim> optClaim = compensationClaimService.findClaim(id);
		if (optClaim.isPresent()) {
			CompensationClaim claim = optClaim.get();
			model.addAttribute("claim", claim);
		}
		return "viewDetailsClaim";
	}
	
	@PostMapping("/confirmOutcome/{id}")
	public String confirmOutcome (@RequestParam("applicationOutcome") String applicationOutcome, @PathVariable("id") Integer id) {
		Optional<CompensationClaim> optClaim = compensationClaimService.findClaim(id);
		if (optClaim.isPresent()) {
			CompensationClaim claim = optClaim.get();
			if (applicationOutcome.equals("Approve")) {
				claim.setStatus(Status.Approved);
			}
			else if (applicationOutcome.equals("Reject")) {
				claim.setStatus(Status.Rejected);
			}
			compensationClaimService.save(claim);
		}
		return "redirect:/compensation/viewClaimsForApproval";
	}
}
