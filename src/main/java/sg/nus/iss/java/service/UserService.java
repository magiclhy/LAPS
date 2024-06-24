package sg.nus.iss.java.service;

import java.util.List;
import java.util.Optional;

import sg.nus.iss.java.model.Employee;
import sg.nus.iss.java.model.User;

public interface UserService {
	User saveUser(User user);
	List<User> findUserByName(String k);
	Optional<User> findUserById(int id);
	Optional<User> findUserByUsername(String username);
	List<Employee> findAllUsersByType(String type);
	void deleteUserById(int id);
	
}
