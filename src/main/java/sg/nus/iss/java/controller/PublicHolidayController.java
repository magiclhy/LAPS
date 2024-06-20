package sg.nus.iss.java.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import sg.nus.iss.java.model.PublicHoliday;
import sg.nus.iss.java.service.PublicHolidayService;

@Controller
@RequestMapping("ph")
public class PublicHolidayController {
	
	@Autowired
	private PublicHolidayService publicHolidayService;
	
	// ADMIN CREATE PUBLIC HOLIDAY DATES
	@GetMapping("/create")
	public String addPH(Model model) {
		model.addAttribute("holiday", new PublicHoliday());
		return "createPH";
	}
	
	@PostMapping("/confirm")
	public String confirmPH(@Valid @ModelAttribute("holiday") PublicHoliday publicHoliday, BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			System.out.println("Binding error");
			return "createPH";
		}
		publicHolidayService.savePH(publicHoliday);
		return "redirect:/ph/view";
	}
	
	// ADMIN VIEW PUBLIC HOLIDAY DATES
	@GetMapping("/view")
	public String viewPH(Model model) {
		List<PublicHoliday> publicHolidays = publicHolidayService.findAllPublicHolidays();
		model.addAttribute("publicHolidays", publicHolidays);
		return "viewPH";
	}
	
	// ADMIN EDIT PUBLIC HOLIDAY
	@GetMapping("/edit/{id}")
	public String editPH (Model model, @PathVariable("id") Integer id) {
		Optional<PublicHoliday> optPublicHoliday = publicHolidayService.findPH(id);
		if (optPublicHoliday.isPresent()) {
			PublicHoliday publicHoliday = optPublicHoliday.get();
			model.addAttribute("holiday", publicHoliday);
		}
		return "editPH";
	}
	
	@PostMapping("/save")
	public String savePH(@Valid @ModelAttribute("holiday") PublicHoliday publicHoliday, BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			System.out.println("Binding error");
			return "editPH";
		}
		publicHolidayService.savePH(publicHoliday);
		return "redirect:/ph/view";
	}
	
	//ADMIN DELETE PUBLIC HOLIDAY
	@GetMapping("/delete/{id}")
	public String deletePH(@PathVariable("id") Integer id) {
		Optional<PublicHoliday> optPublicHoliday = publicHolidayService.findPH(id);
		if (optPublicHoliday.isPresent()) {
			PublicHoliday publicHoliday = optPublicHoliday.get();
			publicHolidayService.deletePH(publicHoliday);
		}
		return "redirect:/ph/view";
	}
}
