package sg.nus.iss.java.service;

import java.util.List;
import java.util.Optional;

import sg.nus.iss.java.model.Employee;

public interface EmployeeService {
	List<Employee> findAllEmployees();
	Optional<Employee> findEmployeeById(int id);
	Employee saveEmployee(Employee employee);
	void deleteEmployee(Employee employee);
}
