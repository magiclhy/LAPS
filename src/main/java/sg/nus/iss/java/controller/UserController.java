package sg.nus.iss.java.controller;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

@Controller
@RequestMapping("users")
public class UserController {
	
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
	
	//CREATE NEW USER
	@GetMapping("/create")
	public String createNewUser(Model model) {
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        String currentYearString = Integer.toString(currentYear);
		List<Manager> managers = managerService.findAllManagers();
		model.addAttribute("managers", managers);
		List<Employee> employees = userService.findAllUsersByType("Employee");
		model.addAttribute("employees", employees);
		List<LeaveQuota> leaveQuotasManager = leaveQuotaService.findLeaveQuota(currentYearString, "Manager");
		model.addAttribute("leaveQuotasManager", leaveQuotasManager);
		List<LeaveQuota> leaveQuotasEmp = leaveQuotaService.findLeaveQuota(currentYearString, "Employee");
		model.addAttribute("leaveQuotasEmp", leaveQuotasEmp);
		model.addAttribute("user", new User());
		return "createUser";
	}
	
	@PostMapping("/create")
	public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, 
	                     @RequestParam("type") String type, @RequestParam("manager") Optional<Integer> managerId, 
	                     @RequestParam("employees") Optional<List<Integer>> employeeIds, 
	                     @RequestParam("leaveQuotaManager") Optional<LeaveQuota> leaveQuotaManager,
	                     @RequestParam("leaveQuotaEmp") Optional<LeaveQuota> leaveQuotaEmp, HttpSession sessionObj, Model model) {
	    if (bindingResult.hasErrors()) {
	        List<Manager> managers = managerService.findAllManagers();
	        model.addAttribute("managers", managers);
	        List<Employee> employees = userService.findAllUsersByType("Employee");
	        model.addAttribute("employees", employees);
	        LocalDate currentDate = LocalDate.now();
	        int currentYear = currentDate.getYear();
	        String currentYearString = Integer.toString(currentYear);
	        List<LeaveQuota> leaveQuotasManager = leaveQuotaService.findLeaveQuota(currentYearString, "Manager");
			model.addAttribute("leaveQuotasManager", leaveQuotasManager);
			List<LeaveQuota> leaveQuotasEmp = leaveQuotaService.findLeaveQuota(currentYearString, "Employee");
			model.addAttribute("leaveQuotasEmp", leaveQuotasEmp);
	        return "createUser";
	    } else {

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
	    }
	    return "redirect:/home";
	}
	
	//VIEW USER DETAILS
	@GetMapping("/search")
	public String searchUser () {
		return "searchUser";
	}
	
	@PostMapping("/search")
	public String displayUsers(@RequestParam("searchBy") String searchType, @RequestParam("keyword") Optional<String> keyword, 
			@RequestParam("type") Optional<String> type, Model model) {
		
		if (searchType.equals("name")) {
			model.addAttribute("users", userService.findUserByName(keyword.get()));
		}
		else if (searchType.equals("type"))
		{
			model.addAttribute("users", userService.findAllUsersByType(type.get()));
		}
		return "searchResults";
	}
	
	//UPDATE USER DETAILS
	@GetMapping("/edit/{id}")
	public String editUserDetails(@PathVariable("id") Integer id, ModelMap model) {
		//Send over the list of managers and employee in case the employee's role will be changed
		//Remove employee from the list of names so they cannot be their own manager/employee
		LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        String currentYearString = Integer.toString(currentYear);
		List<Employee> managers = userService.findAllUsersByType("Manager");
		List<Employee> employees = userService.findAllUsersByType("Employee");
		List<LeaveQuota> leaveQuotasManager = leaveQuotaService.findLeaveQuota(currentYearString, "Manager");
		model.addAttribute("leaveQuotasManager", leaveQuotasManager);
		List<LeaveQuota> leaveQuotasEmp = leaveQuotaService.findLeaveQuota(currentYearString, "Employee");
		model.addAttribute("leaveQuotasEmp", leaveQuotasEmp);
		Optional<User> optUser = userService.findUserById(id);
		if (optUser.isPresent()) {
			User user = optUser.get();
			if (user.getType().equals("Employee")) {
				Employee employee = employeeService.findEmployeeById(id).get();
				model.addAttribute("userType", user.getType());
				model.addAttribute("user", employee);
				model.addAttribute("manager", employee.getManager());
				managers.remove(employee);
				employees.remove(employee);
			}
			else if (user.getType().equals("Manager")) {
				Manager manager = managerService.findManagerById(id).get();
				model.addAttribute("userType", user.getType());
				model.addAttribute("user", manager);
				model.addAttribute("employees", manager.getEmployees());
				managers.remove(manager);
				employees.remove(manager);
			}
			else if (user.getType().equals("Ceo")) {
				Ceo ceo = ceoService.findCeoById(id).get();
				model.addAttribute("userType", user.getType());
				model.addAttribute("user", ceo);
				model.addAttribute("managers", ceo.getManagers());
			}
			else if (user.getType().equals("Admin")) {
				Admin admin = adminService.findAdminById(id).get();
				model.addAttribute("userType", user.getType());
				model.addAttribute("user", admin);
			}
		}
		model.addAttribute("managers", managers);
		model.addAttribute("allEmployees", employees);
		return "editUser";
	}
	
	@PostMapping("/save")
	public String saveUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, 
			@RequestParam("type") String type, @RequestParam("manager") Optional<Integer> managerId, 
			@RequestParam("employees") Optional<List<Integer>> employeeIds, 
			@RequestParam("leaveQuotaManager") Optional<LeaveQuota> leaveQuotaManager,
            @RequestParam("leaveQuotaEmp") Optional<LeaveQuota> leaveQuotaEmp,
            @RequestParam("id") int userId, Model model, @RequestParam("changeRole") String changeRole) {
		if (bindingResult.hasErrors()) {
			List<Manager> managers = managerService.findAllManagers();
	        model.addAttribute("managers", managers);
	        List<Employee> employees = userService.findAllUsersByType("Employee");
	        model.addAttribute("employees", employees);
			LocalDate currentDate = LocalDate.now();
	        int currentYear = currentDate.getYear();
	        String currentYearString = Integer.toString(currentYear);
	        List<LeaveQuota> leaveQuotasManager = leaveQuotaService.findLeaveQuota(currentYearString, "Manager");
			model.addAttribute("leaveQuotasManager", leaveQuotasManager);
			List<LeaveQuota> leaveQuotasEmp = leaveQuotaService.findLeaveQuota(currentYearString, "Employee");
			model.addAttribute("leaveQuotasEmp", leaveQuotasEmp);
			User user1 = userService.findUserById(userId).get();
			if (user1.getType().equals("Employee")) {
				Employee employee = employeeService.findEmployeeById(userId).get();
				model.addAttribute("user", employee);
			}
			else if (user1.getType().equals("Manager")) {
				Manager manager = managerService.findManagerById(userId).get();
				model.addAttribute("user", manager);
				model.addAttribute("employees", manager.getEmployees());
			}
			else if (user1.getType().equals("Ceo")) {
				Ceo ceo = ceoService.findCeoById(userId).get();
				model.addAttribute("user", ceo);
			}
			return "editUser";
		}
		else {
			System.out.println("!!!type: " + type);
			String currentYear = Year.now().toString();
			//LeaveQuota leaveQuota = leaveQuotaService.findLeaveQuota(currentYear, type);
			if (type.equals("Employee")) {
				if (changeRole.equals("yes")) {
					//If role changed, need to change class
					Employee employee = convertUserToEmployee(user, new Employee());
					userService.deleteUserById(userId);
					employee.setId(userId);
					Manager managerOfEmp = managerService.findManagerById(managerId);
					employee.setManager(managerOfEmp);
					//employee.setLeaveQuota(leaveQuota);
					leaveQuotaEmp.ifPresent(employee::setLeaveQuota);
					employeeService.saveEmployee(employee);
				}
				else if (changeRole.equals("no")) {
					//No change in role
					Employee currEmployee = employeeService.findEmployeeById(userId).get();
					Employee employee = convertUserToEmployee(user, currEmployee);
					Manager managerOfEmp = managerService.findManagerById(managerId);
					employee.setManager(managerOfEmp);
					//employee.setLeaveQuota(leaveQuota);
					leaveQuotaEmp.ifPresent(employee::setLeaveQuota);
					employeeService.saveEmployee(employee);
				}
			}
			else if (type.equals("Manager")) {
				if (changeRole.equals("yes")) {
					//If role changed, need to change class
					Manager manager = convertUserToManager(user, new Manager());
					userService.deleteUserById(userId);
					manager.setId(userId);
					leaveQuotaManager.ifPresent(manager::setLeaveQuota);
					if (employeeIds.isPresent()) {
						List<Integer> employeeIdsList = employeeIds.get();
						for (int empId : employeeIdsList) {
							Optional<Employee> optEmployee = employeeService.findEmployeeById(empId);
							if (optEmployee.isPresent()) {
								Employee employee = optEmployee.get();
								employee.setManager(manager);
							}
						}
					}
					//manager.setLeaveQuota(leaveQuota);
					Ceo ceo = ceoService.findCeoById(6).get();
		            manager.setCeo(ceo);
					managerService.saveManager(manager);
				}
				else if (changeRole.equals("no")){
					//No change in role
					Manager currManager = managerService.findManagerById(userId).get();
					Manager manager = convertUserToManager(user, currManager);
					managerService.saveManager(manager);
					leaveQuotaManager.ifPresent(manager::setLeaveQuota);
					if (employeeIds.isPresent()) {
						List<Integer> employeeIdsList = employeeIds.get();
						for (int empId : employeeIdsList) {
							Optional<Employee> optEmployee = employeeService.findEmployeeById(empId);
							if (optEmployee.isPresent()) {
								Employee employee = optEmployee.get();
								employee.setManager(manager);
							}
						}
					}
					//manager.setLeaveQuota(leaveQuota);
					Ceo ceo = ceoService.findCeoById(6).get();
		            manager.setCeo(ceo);
					managerService.saveManager(manager);
				}
			}
			else if (type.equals("Admin")) {
				if (changeRole.equals("yes")) {
					//If role changed, need to change class
					Admin admin = convertUserToAdmin(user, new Admin());
					userService.deleteUserById(userId);
					admin.setId(userId);
					adminService.saveAdmin(admin);
				}
				else if (changeRole.equals("no")) {
					//No change in role
					Admin currAdmin = adminService.findAdminById(userId).get();
					Admin admin = convertUserToAdmin(user, currAdmin);
					adminService.saveAdmin(admin);
				}
			}
			else {
				Ceo currCeo = ceoService.findCeoById(userId).get();
				Ceo ceo = convertUserToCeo(user, currCeo);
				ceoService.saveCeo(ceo);
			}
		}
		return "redirect:/home";
	}
	
	
	//DELETE USER	
	@GetMapping("/delete/{id}")
	public String deleteUser (@PathVariable("id") Integer id) {
		userService.deleteUserById(id);
		return "redirect:/home";
	}
	
	
	
	//METHODS
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
