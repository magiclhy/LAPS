package sg.nus.iss.java.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import sg.nus.iss.java.model.User;
import sg.nus.iss.java.service.UserService;

@RestController
public class AccountRestController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/api/login")
    public ResponseEntity<?> validateLogin(@RequestBody UserLoginRequest request, HttpSession sessionObj) {
        String username = request.getUsername();
        String password = request.getPassword();

        Optional<User> optUser = userService.findUserByUsername(username);
        if (optUser.isPresent()) {
            User user = optUser.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                sessionObj.setAttribute("user", user);
                sessionObj.setAttribute("role", user.getType());
                sessionObj.setAttribute("name", user.getName());
                return ResponseEntity.ok("Login successful");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
    }

    public static class UserLoginRequest {
        private String username;
        private String password;

        // Getters and setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
