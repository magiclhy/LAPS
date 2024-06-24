package sg.nus.iss.java.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sg.nus.iss.java.model.Employee;
import sg.nus.iss.java.repository.EmployeeRepository;

@Service
@Transactional(readOnly=true)
public class EmployeeServiceImpl implements EmployeeService{
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Override
	public List<Employee> findAllEmployees(){
		return employeeRepository.findAll();
	}
	
	@Override
	public Optional<Employee> findEmployeeById(int id){
		return employeeRepository.findById(id);
	}
	

	@Override
	@Transactional (readOnly=false)
	public Employee saveEmployee(Employee employee) {
		return employeeRepository.save(employee);
	}

	
	@Override
	@Transactional (readOnly=false)
	public void deleteEmployee(Employee employee) {
		employeeRepository.delete(employee);
	}
}
