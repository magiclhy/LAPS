package sg.nus.iss.java.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sg.nus.iss.java.model.Employee;
import sg.nus.iss.java.model.User;
import sg.nus.iss.java.repository.UserRepository;

@Service
@Transactional(readOnly=true)
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Override
	@Transactional(readOnly=false)
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public List<User> findUserByName(String k) {
		return userRepository.findUserByName(k);
	}

	@Override
	public Optional<User> findUserById(int id) {
		return userRepository.findById(id);
	}

	@Override
	public Optional<User> findUserByUsername(String username) {
		return userRepository.findUserByUsername(username);
	}

	@Override
	public List<Employee> findAllUsersByType(String type) {
		return userRepository.findAllUsersByType(type);
	}

	@Override
	@Transactional(readOnly=false)
	public void deleteUserById(int id) {
		userRepository.deleteById(id);
	}
}
