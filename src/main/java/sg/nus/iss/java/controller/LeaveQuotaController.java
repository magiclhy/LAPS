package sg.nus.iss.java.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import sg.nus.iss.java.model.LeaveQuota;
import sg.nus.iss.java.service.LeaveQuotaService;
import sg.nus.iss.java.model.cyValidator;

@Controller
@RequestMapping("leaveQuota")
public class LeaveQuotaController {
	@Autowired
	private LeaveQuotaService leaveQuotaService;
	
	@Autowired
    private cyValidator cyValidator;
	
	@InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(cyValidator);
    }
	//CREATE LEAVE QUOTA
	@GetMapping("/create")
	public String createLeaveQuota (Model model) {
		model.addAttribute("leaveQuota", new LeaveQuota());
		return "createLeaveQuota";
	}
	
	@PostMapping("/create")
	public String confirmLeaveQuota(@Valid @ModelAttribute LeaveQuota leaveQuota, BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			System.out.println("Error binding :(");
			return "createLeaveQuota";
		}
		leaveQuotaService.saveLeaveQuota(leaveQuota);
		return "redirect:/leaveQuota/view";
	}
	
	//VIEW LEAVE QUOTA
	@GetMapping("/view")
	public String viewLeaveQuota(Model model) {
		List<LeaveQuota> leaveQuotas = leaveQuotaService.findAllLeaveQuotas();
		model.addAttribute("leaveQuotas", leaveQuotas);
		return "viewLeaveQuota";
	}
	
	//EDIT LEAVE QUOTA
	@GetMapping("/edit/{id}")
	public String editLeaveQuota (Model model, @PathVariable("id") Integer id) {
		Optional<LeaveQuota> optLeaveQuota = leaveQuotaService.findLeaveQuota(id);
		if (optLeaveQuota.isPresent()) {
			LeaveQuota leaveQuota = optLeaveQuota.get();
			model.addAttribute("leaveQuota", leaveQuota);
		}
		return "editLeaveQuota";
	}
	
	@PostMapping("/save")
	public String saveLeaveQuota(@Valid @ModelAttribute LeaveQuota leaveQuota, BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			System.out.println("Binding error");
			return "editLeaveQuota";
		}
		leaveQuotaService.saveLeaveQuota(leaveQuota);
		return "redirect:/leaveQuota/view";
	}
	
	//DELETE LEAVE QUOTA
	@GetMapping("/delete/{id}")
	public String deleteLeaveQuota(@PathVariable("id") Integer id) {
		Optional<LeaveQuota> optLeaveQuota = leaveQuotaService.findLeaveQuota(id);
		if (optLeaveQuota.isPresent()) {
			LeaveQuota leaveQuota = optLeaveQuota.get();
			leaveQuotaService.deleteLeaveQuota(leaveQuota);
		}
		return "redirect:/leaveQuota/view";
	}
}
