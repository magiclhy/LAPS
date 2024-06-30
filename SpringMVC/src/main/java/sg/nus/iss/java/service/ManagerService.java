package sg.nus.iss.java.service;

import java.util.List;
import java.util.Optional;

import sg.nus.iss.java.model.Manager;

public interface ManagerService {
	Manager saveManager(Manager manager);
	Optional<Manager> findManagerById(int id);
	void deleteManager(Manager manager);
	List<Manager> findAllManagers();
	Manager findManagerById(Optional<Integer> id);
}
