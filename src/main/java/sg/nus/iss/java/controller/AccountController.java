package sg.nus.iss.java.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import sg.nus.iss.java.model.User;
import sg.nus.iss.java.service.UserService;

@Controller
public class AccountController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping({"/", "/login"})
	public String login(HttpSession sessionObj) {
		if (sessionObj.getAttribute("user") != null) {
			return "redirect:/home";
		}
		return "login";
	}
	
	@PostMapping("/login")
	public String validateLogin(@RequestParam("username") String username, @RequestParam("password") String password, Model model, HttpSession sessionObj) {
		Optional<User> optUser = userService.findUserByUsername(username);
		if (optUser.isPresent()) {
			User user = optUser.get();
			if (user.getPassword().equals(password)) {
				//After authenticated, save user into session for future use
				sessionObj.setAttribute("user", user);
				sessionObj.setAttribute("role", user.getType());
				sessionObj.setAttribute("name", user.getName());
				return "redirect:/home";
			}
		}
		model.addAttribute("message", "Incorrect username or password, please try again.");
		return "login";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession sessionObj) {
		sessionObj.removeAttribute("user");
		return "redirect:/";
	}
	
	@GetMapping("/home")
	public String showHome(HttpSession sessionObj) {
		if (sessionObj.getAttribute("user") == null) {
			return "redirect:/login";
		}
		return "home";
	}
}
