package sg.nus.iss.java.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sg.nus.iss.java.model.Manager;
import sg.nus.iss.java.repository.ManagerRepository;

@Service
@Transactional(readOnly=true)
public class ManagerServiceImpl implements ManagerService{
	@Autowired
	private ManagerRepository managerRepository;

	@Transactional (readOnly=false)
	@Override
	public Manager saveManager(Manager manager) {
		return managerRepository.save(manager);
	}

	@Override
	public Optional<Manager> findManagerById(int id) {
		return managerRepository.findById(id);
	}
	
	@Transactional (readOnly=false)
	@Override
	public void deleteManager(Manager manager) {
		managerRepository.delete(manager);
	}

	@Override
	public List<Manager> findAllManagers() {
		return managerRepository.findAllManagers();
	}

	@Override
	public Manager findManagerById(Optional<Integer> id) {
		return managerRepository.findManagerById(id);
	}

}
