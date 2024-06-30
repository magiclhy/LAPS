package sg.nus.iss.java.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
			System.out.println(username);
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

//	@PostMapping(value = "/", consumes = "application/json")
//	public ResponseEntity<String> validateLogin(@RequestBody Map<String, String> payload, HttpSession sessionObj) {
//		String username = payload.get("username");
//		String password = payload.get("password");
//
//		Optional<User> optUser = userService.findUserByUsername(username);
//		if (optUser.isPresent()) {
//			User user = optUser.get();
//			if (user.getPassword().equals(password)) {
//				sessionObj.setAttribute("user", user);
//				sessionObj.setAttribute("role", user.getType());
//				sessionObj.setAttribute("name", user.getName());
//				return ResponseEntity.ok("Login successful");
//			}
//		}
//		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password, please try again.");
//	}
	
	@GetMapping("/logout")
    public String logout(HttpSession sessionObj) {
        sessionObj.invalidate(); // 

        
        if (isReactAppRunning()) {
            return "redirect:http://localhost:3000";
        } else {
            return "redirect:/login";
        }
    }
	private boolean isReactAppRunning() {
        try {
            URL url = new URL("http://localhost:3000");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return (200 <= responseCode && responseCode <= 399);
        } catch (IOException e) {
            return false;
        }
    }
	
	@GetMapping("/home")
	public String showHome(HttpSession sessionObj) {
		if (sessionObj.getAttribute("user") == null) {
			return "redirect:/login";
		}
		return "home";
	}
}
