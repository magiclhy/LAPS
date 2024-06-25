package sg.nus.iss.java.api;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import sg.nus.iss.java.model.Admin;
import sg.nus.iss.java.model.Ceo;
import sg.nus.iss.java.model.Employee;
import sg.nus.iss.java.model.LeaveQuota;
import sg.nus.iss.java.model.Manager;
import sg.nus.iss.java.model.User;
import sg.nus.iss.java.service.AdminService;
import sg.nus.iss.java.service.CeoService;
import sg.nus.iss.java.service.EmployeeService;
import sg.nus.iss.java.service.LeaveQuotaService;
import sg.nus.iss.java.service.ManagerService;
import sg.nus.iss.java.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    
    @Autowired
    private UserService userService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private CeoService ceoService;
    @Autowired
    private LeaveQuotaService leaveQuotaService;

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody @Valid User user, 
                                         @RequestParam("type") String type, 
                                         @RequestParam("manager") Optional<Integer> managerId, 
                                         @RequestParam("employees") Optional<List<Integer>> employeeIds, 
                                         @RequestParam("leaveQuotaManager") Optional<LeaveQuota> leaveQuotaManager,
                                         @RequestParam("leaveQuotaEmp") Optional<LeaveQuota> leaveQuotaEmp) {
        if (type.equals("Employee")) {
            Employee employee = convertUserToEmployee(user, new Employee());
            managerId.ifPresent(id -> {
                Optional<Manager> managerOptional = managerService.findManagerById(id);
                managerOptional.ifPresent(employee::setManager);
            });
            leaveQuotaEmp.ifPresent(employee::setLeaveQuota);
            employeeService.saveEmployee(employee);
        } else if (type.equals("Manager")) {
            Manager manager = convertUserToManager(user, new Manager());
            leaveQuotaManager.ifPresent(manager::setLeaveQuota);
            if (employeeIds.isPresent()) {
                List<Integer> employeeIdsList = employeeIds.get();
                for (int empId : employeeIdsList) {
                    Optional<Employee> optEmployee = employeeService.findEmployeeById(empId);
                    optEmployee.ifPresent(employee -> {
                        employee.setManager(manager);
                        employeeService.saveEmployee(employee);
                    });
                }
            }
            Ceo ceo = ceoService.findCeoById(6).get();
            manager.setCeo(ceo);
            managerService.saveManager(manager);
        } else if (type.equals("Admin")) {
            Admin admin = convertUserToAdmin(user, new Admin());
            adminService.saveAdmin(admin);
        }
        return ResponseEntity.ok("User created successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Integer id) {
        Optional<User> user = userService.findUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.status(404).body(null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable("id") Integer id, @RequestBody @Valid User user,
                                             @RequestParam("type") String type, @RequestParam("manager") Optional<Integer> managerId, 
                                             @RequestParam("employees") Optional<List<Integer>> employeeIds, 
                                             @RequestParam("leaveQuotaManager") Optional<LeaveQuota> leaveQuotaManager,
                                             @RequestParam("leaveQuotaEmp") Optional<LeaveQuota> leaveQuotaEmp) {
        Optional<User> optUser = userService.findUserById(id);
        if (optUser.isPresent()) {
            User existingUser = optUser.get();
            if (existingUser.getType().equals("Employee")) {
                Employee currEmployee = employeeService.findEmployeeById(id).get();
                Employee employee = convertUserToEmployee(user, currEmployee);
                managerId.ifPresent(managerService::findManagerById);
                leaveQuotaEmp.ifPresent(employee::setLeaveQuota);
                employeeService.saveEmployee(employee);
            } else if (existingUser.getType().equals("Manager")) {
                Manager currManager = managerService.findManagerById(id).get();
                Manager manager = convertUserToManager(user, currManager);
                leaveQuotaManager.ifPresent(manager::setLeaveQuota);
                managerService.saveManager(manager);
            } else if (existingUser.getType().equals("Admin")) {
                Admin currAdmin = adminService.findAdminById(id).get();
                Admin admin = convertUserToAdmin(user, currAdmin);
                adminService.saveAdmin(admin);
            } else if (existingUser.getType().equals("Ceo")) {
                Ceo currCeo = ceoService.findCeoById(id).get();
                Ceo ceo = convertUserToCeo(user, currCeo);
                ceoService.saveCeo(ceo);
            }
            return ResponseEntity.ok("User updated successfully");
        }
        return ResponseEntity.status(404).body("User not found");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    public Employee convertUserToEmployee(User user, Employee newEmployee) {
        newEmployee.setName(user.getName());
        newEmployee.setUsername(user.getUsername());
        newEmployee.setPassword(user.getPassword());
        return newEmployee;
    }
    
    public Manager convertUserToManager(User user, Manager newManager) {
        newManager.setName(user.getName());
        newManager.setUsername(user.getUsername());
        newManager.setPassword(user.getPassword());
        return newManager;
    }
    
    public Admin convertUserToAdmin(User user, Admin newAdmin) {
        newAdmin.setName(user.getName());
        newAdmin.setUsername(user.getUsername());
        newAdmin.setPassword(user.getPassword());
        return newAdmin;
    }
    
    public Ceo convertUserToCeo(User user, Ceo ceo) {
        ceo.setName(user.getName());
        ceo.setUsername(user.getUsername());
        ceo.setPassword(user.getPassword());
        return ceo;
    }
}
