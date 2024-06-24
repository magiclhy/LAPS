package sg.nus.iss.java.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sg.nus.iss.java.model.Admin;
import sg.nus.iss.java.repository.AdminRepository;

@Service
@Transactional(readOnly=true)
public class AdminServiceImpl implements AdminService {
	@Autowired
	private AdminRepository adminRepository;
	
	@Override
	@Transactional(readOnly=false)
	public Admin saveAdmin(Admin admin) {
		return adminRepository.save(admin);
	}

	@Override
	public Optional<Admin> findAdminById(int id) {
		return adminRepository.findById(id);
	}

}
