package sg.nus.iss.java.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import sg.nus.iss.java.model.User;
import sg.nus.iss.java.service.UserService;

@RestController
@RequestMapping("/api/account")
public class AccountRestController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/login")
    public ResponseEntity<String> validateLogin(@RequestParam("username") String username, 
                                                @RequestParam("password") String password, 
                                                HttpSession sessionObj) {
        Optional<User> optUser = userService.findUserByUsername(username);
        if (optUser.isPresent()) {
            User user = optUser.get();
            if (user.getPassword().equals(password)) {
                //After authenticated, save user into session for future use
                sessionObj.setAttribute("user", user);
                sessionObj.setAttribute("role", user.getType());
                sessionObj.setAttribute("name", user.getName());
                return ResponseEntity.ok("Login successful");
            }
        }
        return ResponseEntity.status(401).body("Incorrect username or password, please try again.");
    }
    
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession sessionObj) {
        sessionObj.removeAttribute("user");
        return ResponseEntity.ok("Logout successful");
    }
    
    @GetMapping("/home")
    public ResponseEntity<String> showHome(HttpSession sessionObj) {
        if (sessionObj.getAttribute("user") == null) {
            return ResponseEntity.status(401).body("Unauthorized, please login.");
        }
        return ResponseEntity.ok("Welcome to home page");
    }
}
